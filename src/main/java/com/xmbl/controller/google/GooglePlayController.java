package com.xmbl.controller.google;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.xmbl.base.BaseController;
import com.xmbl.config.GoogleConfig;
import com.xmbl.constant.CommonConstant;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.GoogleTranspondRecord;
import com.xmbl.service.pay.GoogleTranspondRecordService;
import com.xmbl.service.pay.RechargeRecordService;
import com.xmbl.util.DateUtils;
import com.xmbl.util.HttpUtils;
import com.xmbl.util.OrderGeneratedUtils;
import com.xmbl.util.RSASignature;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import com.xmbl.web.api.bean.PageData;
import com.xmbl.web.api.bean.Response;
import com.xmbl.web.api.bean.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(Route.PATH + Route.Payment.PATH)
public class GooglePlayController extends BaseController {

    private static final String GOOGLE_PLAY_URL = "https://www.googleapis.com/androidpublisher/v3"; // google play
    private static final String GOOGLE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwqaoSbYV8slHU/ayhOb9c+8zt/20UwaJ+M2iM8fjBsaxMviy7fAqIAmehtNgJn8664AaAT9Ms7LQjv1Dl6QUK4f7KKmF6g6yQbrtvM3HwcQxLSumGKa5KqoKTBf2VqwkY91Pl9FmWk0oyWIQRGNjzjUUJUUV2gSU/4PdQHDeeVghyssJ4AKUhQ1rfuklUBLpH2OMMTrqNssYi/oSMNTdsIJYw/goUuefHjIdAPGUVJ2Jdcx0ZidSsjyQY19taJIju1BwnRZQCLpQKFyGwu+BH0C9qzRNUrivFR+svcfWmxKFLPLvVmpHn6rUrkY6mpXChurnjIn0DJ9XRd08o47eQQIDAQAB"; // google play

    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private GoogleTranspondRecordService googleTranspondRecordService;

    /**
     * google in-app-billing订单验证
     * 根据app的公钥验证订单合法性
     * 1
     *
     * @param purchase_data
     * @param signature
     * @return
     */
    public boolean check(String purchase_data, String signature) {
        return RSASignature.doCheck(purchase_data, signature, GOOGLE_PUBLIC_KEY);
    }

    /**
     * 验证Google内购订单
     *
     * @param jsonData
     * @return
     */
    @PostMapping(value = Route.Payment.VERIFY_GOOGLE_ORDER)
    public ResponseResult verify_order(HttpServletRequest request, String jsonData) {
        log.info("infoMsg:--- 验证Google内购订单开始==================== jsonData,{}", JSON.toJSONString(jsonData));
        Response reponse = this.getReponse();
        int status = 0;
        try {
            // single.ec 验证支付请求参数 不为空
            Assert.isTrue(jsonData != null, EnumInfrastructureResCode.PARAMETER_FORMAT_ERROR.code());
            // Step1. 解析客户端的请求的订单信息
            JSONObject orderObj = JSON.parseObject(jsonData);
            Long PlayerId = orderObj.getLong("PlayerId");
            String AccountId = orderObj.getString("AccountId");
            String userkey = orderObj.getString("userkey");
            String purchaseInfo = orderObj.getString("purchaseInfo");
            JSONObject purchaseInfos = JSON.parseObject(purchaseInfo);
            String originalOrderId = purchaseInfos.getString("orderId");   // Google服务商订单ID
            // 检测是否有重复订单
            if (!rechargeRecordService.findRepeatOrderByOriginalOrderId(originalOrderId, CommonConstant.GOOGLE_WAYTD)) {
                return errorJson(EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.value(), EnumInfrastructureResCode.ORDER_HAS_BEEN_SHIPPED.code());
            }
            String packageName = purchaseInfos.getString("packageName");
            String productId = purchaseInfos.getString("productId");
            Assert.isTrue(productId.substring(0, 18).equals("com.boluogame.fkcz"), EnumInfrastructureResCode.GOOGLE_PLAY_INFO_NOT_NULL.code());
            String goodID = productId.substring(20);
//			String purchaseTime = purchaseInfos.getString("purchaseTime");
//			int purchaseState = purchaseInfos.getInteger("purchaseState");
//			String developerPayload = purchaseInfos.getString("developerPayload");
            String purchasetoken = purchaseInfos.getString("purchaseToken");
            // single.ec 验证支付请求参数 不为空
//			Assert.isTrue(purchaseInfo != null && check(purchaseInfo, purchasetoken), EnumInfrastructureResCode.GOOGLE_PLAY_INFO_NOT_NULL.code());
            GoogleTranspondRecord record = googleTranspondRecordService.findLasted();
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

            PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
                    SecurityUtils.getPkcs12KeyStore(),
                    new FileInputStream(new File(GoogleConfig.SSL_JKS_PATH)), // 生成的P12文件
                    "notasecret", "privateKey", "notasecret"); //wss

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(transport).setJsonFactory(JacksonFactory.getDefaultInstance())
                    .setServiceAccountId(GoogleConfig.ACCOUNT_ID) // e.g.: 626891557797-frclnjv31rn4ss81ch746g9t6pd3mmej@developer.gserviceaccount.com
                    .setServiceAccountScopes(AndroidPublisherScopes.all())
                    .setServiceAccountPrivateKey(privateKey).build();

            AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
                    JacksonFactory.getDefaultInstance(), credential.setAccessToken(record.getAccess_token())).build();

            AndroidPublisher.Purchases.Products products = publisher.purchases().products();

            // 参数详细说明: https://developers.google.com/android-publisher/api-ref/purchases/products/get
            AndroidPublisher.Purchases.Products.Get product = products.get(packageName, productId, purchasetoken);

            // 获取订单信息
            // 返回信息说明: https://developers.google.com/android-publisher/api-ref/purchases/products
            // 通过consumptionState, purchaseState可以判断订单的状态
            ProductPurchase purchase = product.execute();
            log.info("Google Play Response Infos. purchase,{}", JSON.toJSONString(purchase));
//            String google_play = GOOGLE_PLAY_URL + "/applications/" + packageName + "/purchases" + "/products/" + productId + "/tokens/" + purchasetoken + "?access_token=" + record.getAccess_token();
            /*https://www.googleapis.com/androidpublisher/v3/applications/ packageName / purchases / products / productId / tokens / token*/
//            String sendPost = sendPost(google_play);
//            log.info("Google内购订单返回值,reciveMsg,{}", sendPost.toString());
//            JSONObject object = JSON.parseObject(sendPost);

            status = purchase.getPurchaseState();
            if (status == 0) {
                // Step2. 通过产品id 获取商品信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> goodMap = XiaomiGoodsInfoUtil
                        .getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst, "id", goodID);
                log.info("通过产品id获取如下信息:" + goodMap.toString());
                Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.OBTAIN_PRODUCT_INFO_FAILURE.code());
                String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")));
                // Step3. 准备步骤： 提供要参与签名和要加密的参数
                String outTradeNo = OrderGeneratedUtils.getOrderNo(); // 订单号

                JSONObject jsonObj = new JSONObject();
                jsonObj.put("PlayerId", PlayerId);
                jsonObj.put("AccountId", AccountId);
                jsonObj.put("userKey", userkey);
                jsonObj.put("goodID", goodID);
                jsonObj.put("goodNum", 1);
                String params = jsonObj.toJSONString();
                // 充值记录
                // Step4. 生成支付宝充值订单
                rechargeRecordService.generatedGoogleBills(params, Amount, originalOrderId, outTradeNo);
                // 账变，修改状态，到账提醒
                boolean result = rechargeRecordService.updateGoogleBill(params, Amount, originalOrderId, outTradeNo);
                if (result) {
                    log.info("infoMsg:================ 平台订单生成成功!,orderTime,{}",
                            DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
                    return successJson(status, null);
                } else {
                    log.info("infoMsg:================ 平台订单生成失败!,orderTime,{}",
                            DateUtils.formatDate(new Date(), "YYYY:MM:dd HH:mm:ss"));
                    return errorJson(status, "");
                }
            }
            log.info("infoMsg:--- 验证Google内购订单结束");
            return successJson(status, null);
        } catch (Exception e) {
            log.error("errorMsg:{--- 验证Google内购订单失败: errorMsg,{}", e.getMessage() + "---}");
            return errorJson(status, e.getMessage());
        }
    }

    /**
     * @return
     */
    @GetMapping(Route.Payment.GOOGLE_REDIRECT_URL)
    public ResponseResult redirected(HttpServletRequest request) {
        PageData pageData = this.getPageData();
        try {
            Map<String, String> params = rechargeRecordService.parseResponseReferToMap(request);
            // 浏览器将被重定向到带有code 参数的重定向URI，该参数看起来类似于4/eWdxD7b-YSQ5CNNb-c2iI83KQx19.wp6198ti5Zc7dJ3UXOl0T3aRLxQmbwI
            String code = params.get("code");
            return successJson(code);
        } catch (Exception e) {
            return errorJson(e.getMessage());
        }
    }

    @PostMapping(Route.Payment.GET_GOOGLE_ACCESS_TOKEN)
    public ResponseResult getGoogleAccessToken(HttpServletRequest request, String code) {
        log.info("异步获取GoogleAccesstoken, code, {}", code);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        try {
            String grant_type = "authorization_code";
            String client_id = GoogleConfig.CLIENT_ID;
            String client_secret = GoogleConfig.CLIENT_SECRET;
            // HTTP 获取access_token
            String url = "https://accounts.google.com/o/oauth2/token";
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            headers.put("Accept", "text/plain;charset=utf-8");
            querys.put("grant_type", grant_type);
            querys.put("code", code);
            querys.put("client_id", client_id);
            querys.put("client_secret", client_secret);
            querys.put("redirect_uri", GoogleConfig.REDIRECT_URI);
            String googleAccessToken = HttpUtils.send(url, headers, querys, "UTF-8");
            log.info("googleAccessToken====================== ,{},", googleAccessToken);
            JSONObject tokenObject = JSON.parseObject(googleAccessToken);
            String access_token = tokenObject.getString("access_token");
            String token_type = tokenObject.getString("token_type");
            Long expires_in = tokenObject.getLong("expires_in");
            String refresh_token = tokenObject.getString("refresh_token");
            GoogleTranspondRecord record = new GoogleTranspondRecord(code, access_token, refresh_token, token_type, expires_in, CommonConstant.Normal);
            googleTranspondRecordService.saveRecord(record);
            log.info("获取Google_access_token 成功，并入库。");
            return successJson();
        } catch (Exception e) {
            log.error("获取googleAccessToken失败，{}", e.getMessage());
            return errorJson(e.getMessage());
        }
    }

//    public static void main(String[] args) throws Exception {
//
//        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
//
//        PrivateKey privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(
//                SecurityUtils.getPkcs12KeyStore(),
//                new FileInputStream(new File("{P12 key file}")), // 生成的P12文件
//                "notasecret", "privatekey", "notasecret");
//
//        GoogleCredential credential = new GoogleCredential.Builder()
//                .setTransport(transport).setJsonFactory(JacksonFactory.getDefaultInstance())
//                .setServiceAccountId(GoogleConfig.CLIENT_ID) // e.g.: 626891557797-frclnjv31rn4ss81ch746g9t6pd3mmej@developer.gserviceaccount.com
//                .setServiceAccountScopes(AndroidPublisherScopes.all())
//                .setServiceAccountPrivateKey(privateKey).build();
//
//        AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
//                JacksonFactory.getDefaultInstance(), credential.setAccessToken("ya29.GluVBsI9P0zL9kjwuGrGAZOz6ZmhTm46mvAY20k5qT0Cd-Wss5U2eiSOHoxqmMIf_A5HOKVdfHmchDCjuhPFaIpHWFQLALpoe0XBcc1F4SWMG4w1ZgO-4nCV9xnZ")).build();
//
//        AndroidPublisher.Purchases.Products products = publisher.purchases().products();
//
//        // 参数详细说明: https://developers.google.com/android-publisher/api-ref/purchases/products/get
//        AndroidPublisher.Purchases.Products.Get product = products.get("{packageName}",
//                "{productId}", "{token}");
//
//        // 获取订单信息
//        // 返回信息说明: https://developers.google.com/android-publisher/api-ref/purchases/products
//        // 通过consumptionState, purchaseState可以判断订单的状态
//        ProductPurchase purchase = product.execute();
//        log.info("purchase", purchase);
//        purchase.getPurchaseState();
//    }

}
