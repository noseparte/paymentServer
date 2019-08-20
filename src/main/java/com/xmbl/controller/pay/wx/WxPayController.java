package com.xmbl.controller.pay.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.base.BaseController;
import com.xmbl.config.ClientCustomSSL;
import com.xmbl.config.WxpayConfig;
import com.xmbl.constant.CommonConstant;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumInfrastructureResCode;
import com.xmbl.model.ServiceRepository;
import com.xmbl.service.ServiceRepesitoryService;
import com.xmbl.service.pay.RechargeRecordService;
import com.xmbl.service.pay.ThirdPayService;
import com.xmbl.service.pay.TransferRecordService;
import com.xmbl.service.user.AppUserService;
import com.xmbl.util.*;
import com.xmbl.util.MaryunHttpUtils.UHeader;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import com.xmbl.web.api.bean.Route;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2018年7月20日 -- 下午1:14:47
 * @Version 1.0
 * @Description 微信 APP支付
 */
@Slf4j
@Controller
@RequestMapping(value = Route.PATH + Route.Payment.PATH)
public class WxPayController extends BaseController {

    @Autowired
    private RechargeRecordService rechargeRecordService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private TransferRecordService transferRecordService;
    @Autowired
    private ThirdPayService thirdPayService;
    @Autowired
    private ServiceRepesitoryService ServiceRepesitoryService;

    /**
     * 微信统一下单请求交易
     *
     * @param //PlayerId  用户ID
     * @param //AccountId 账号ID
     * @param //userKey   登录名
     * @param //goodID    道具ID
     * @param //Amount    充值金额
     * @return
     */
    @RequestMapping(value = Route.Payment.WX_PAY, method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult wx_pay(HttpServletRequest request, String jsonData) {
        log.info("infoMsg:================ 微信统一下单请求交易开始 ==================== jsonData,{}", JSON.toJSONString(jsonData));
        String message = "";
        String sign = ""; // 签名
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("serviceId").is("7"));
            query.addCriteria(Criteria.where("serviceName").is("recharge"));
            ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
            if (service.getStatus() == 1) {
                return errorJson("充值功能维护中,请稍后再试.");
            }
            // Step1. 解析客户端的请求的订单信息
            JSONObject orderObj = JSON.parseObject(jsonData);
            Long PlayerId = orderObj.getLong("PlayerId");
            String AccountId = orderObj.getString("AccountId");
            String userKey = orderObj.getString("userKey");
            String goodID = orderObj.getString("goodID");
            int goodNum = orderObj.getInteger("goodNum");
            if (String.valueOf(goodNum).equals("") || String.valueOf(goodNum) == null) {
                goodNum = 1;
            }

            // 通过产品id获取如下信息
            log.info("通过产品id获取如下信息开始:产品id:" + goodID);
            Map<String, String> goodMap = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                    "id", goodID);
            log.info("通过产品id获取如下信息:" + goodMap.toString());
            Assert.isTrue(goodMap.size() > 0, EnumInfrastructureResCode.PLATFORM_INFO_NOT_NULL.code());
            String goodName = goodMap.get("notice");
            String Amount = String.valueOf(Double.parseDouble(goodMap.get("cost")) * goodNum);

            // 准备步骤： 提供要参与签名和要加密的参数
            String APP_ID = WxpayConfig.APP_ID; // 应用ID
            String App_Key = WxpayConfig.APP_KEY; // 应用秘钥
            String mch_Id = WxpayConfig.MCH_ID; // 商户ID
            String nonce_str = WXPayUtil.generateNonceStr(); // 随机字符串
            String notify_url = WxpayConfig.NOTIFY_URL; // 通知地址
            String trade_type = "APP"; // 签名
            String body = goodName; // 商品描述 ps:腾讯充值中心-QQ会员充值
            // String body = "diamond"; //商品描述 ps:腾讯充值中心-QQ会员充值
            // body = new String(body.getBytes("ISO-8859-1"),"UTF-8");
            String out_trade_no = OrderGeneratedUtils.getOrderNo(); // 商户订单号
            int total_fee = WXRandomNumberUtil.wx_format_PayAmount(Amount); // 交易金额
            String spbill_create_ip = InetAddress.getLocalHost().getHostAddress(); // 终端IP
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PlayerId", PlayerId);
            jsonObject.put("AccountId", AccountId);
            jsonObject.put("userKey", userKey);
            jsonObject.put("goodID", goodID);
            jsonObject.put("goodNum", goodNum);
            String attach = PlayerId + "|" + AccountId + "|" + userKey + "|" + goodID + "|" + goodNum; // 附加数据
            // ps:用户ID|账号ID|userKey|goodID

            // Step2. 拼裝要签名的参数 params
            StringBuffer sb = new StringBuffer();
            sb.append("appid=").append(APP_ID).append("&"); // 应用ID
            sb.append("attach=").append(attach).append("&");
            sb.append("body=").append(body).append("&");
            sb.append("mch_id=").append(mch_Id).append("&"); // 商户号
            sb.append("nonce_str=").append(nonce_str).append("&");
            sb.append("notify_url=").append(notify_url).append("&"); // 通知地址
            sb.append("out_trade_no=").append(out_trade_no).append("&");
            sb.append("spbill_create_ip=").append(spbill_create_ip).append("&");
            sb.append("total_fee=").append(total_fee).append("&");
            sb.append("trade_type=").append(trade_type); // 交易类型
            String params = sb.toString();
            log.info("infoMsg:================ 微信统一下单未拼装key前的签名 ==================== params,{}",
                    JSON.toJSONString(params));

            // 需要签名的数据
            String stringSignTemp = params + "&key=" + App_Key;
            // Step3. MD5加密方式，向微信服务器发送的请求参数
            sign = WXPayUtil.MD5(stringSignTemp).toUpperCase();
            Map<String, String> map = new HashMap<>();
            map.put("appid", APP_ID);
            map.put("attach", attach);
            map.put("body", body);
            map.put("mch_id", mch_Id);
            map.put("nonce_str", nonce_str);
            map.put("notify_url", notify_url);
            map.put("out_trade_no", out_trade_no);
            map.put("sign", sign);
            map.put("spbill_create_ip", spbill_create_ip);
            map.put("total_fee", String.valueOf(total_fee));
            map.put("trade_type", trade_type);

            String url = String.format(WxpayConfig.gatewayUrl);
            String xmlstring = WXPayUtil.mapToXml(map);
            // 其实不用单独对body进行编码，只要对组装好的整体xml进行编码就行了：
            // xmlstring = new String(xmlstring.getBytes(), "utf-8");
            // xmlstring = new String(xmlstring.toString().getBytes("UTF-8"), "ISO-8859-1");
            log.info("infoMsg:================ 微信统一下单提交的签名 ==================== xmlstring,{}",
                    JSON.toJSONString(xmlstring));
            // Step3. MD5加密方式，向微信服务器发送的请求参数
            List theaderList = new ArrayList<>();
            theaderList.add(new UHeader("Content-Type", "application/x-www-form-urlencoded"));
            // Httpclient发送请求
            String postResponse = MaryunHttpUtils.getPostResponse(url, xmlstring, theaderList);
            // String postRespons1e = HttpUtil.sendPostUrl(url,String.format(xmlstring));
            log.info("infoMsg:================ 微信充值服务器返回的信息 ==================== postResponse,{}",
                    JSON.toJSONString(postResponse));

            Map<String, String> response = WXPayUtil.xmlToMap(new String(postResponse.getBytes("ISO-8859-1"), "UTF-8"));
            if (!response.isEmpty() && response.get("return_code").equals("SUCCESS")) {
                if (response.get("result_code").equals("SUCCESS")) {
                    boolean result = rechargeRecordService.generatedBills(attach, Amount, out_trade_no);
                    if (result) {
                        log.info("InfoMsg:--- 微信统一下单请求交易成功");
                        // 拼装返回客户端的result
                        JSONObject object = new JSONObject();
                        object.put("nonce_str", response.get("nonce_str"));
                        object.put("sign", response.get("sign"));
                        object.put("prepay_id", response.get("prepay_id"));
                        object.put("timestamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));
                        return successJson(object);
                    }
                } else {
                    message = response.get("err_code_des");
                    log.error("errorMsg:--- 微信统一下单请求交易解析失败" + message);
                    return errorJson(message);
                }
            } else {
                log.error("errorMsg:--- 微信统一下单请求交易解析失败" + message);
                return errorJson(message);
            }
            log.info("infoMsg:================ 微信统一下单请求交易结束 ====================");
            return successJson();
        } catch (Exception e) {
            log.error("errorMsg:{================ 微信统一下单请求交易失败:" + e.getMessage() + "====================}");
            return errorJson(e.getMessage());
        }

    }

    /**
     * 微信异步通知
     *
     * @param request
     * @return
     */
    @RequestMapping(value = Route.Payment.WX_PAY_NOTIFY, method = RequestMethod.POST)
    @ResponseBody
    public String wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        log.info("infoMsg:================ 微信异步通知请求开始 ====================");
        BufferedReader reader = null;
        String line = "";
        String wx_map = "";
        try {
            // Step1. 读取微信异步通知的字节流
            reader = request.getReader();
            String xmlString = null;
            StringBuffer inputString = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                inputString.append(line);
            }
            xmlString = inputString.toString();
            if (!StringUtils.isBlank(xmlString)) {
                // Step2. 验证签名环节 微信系统工具类
                /**
                 * WXPayUtil.isSignatureValid(String xmlStr, String key)
                 *
                 * @Param xmlString XML格式数据
                 * @Param key API密钥
                 */
                boolean isSignature = WXPayUtil.isSignatureValid(xmlString, WxpayConfig.APP_KEY);
                if (isSignature) {
                    log.info("infoMsg:================ 微信异步通知验证签名成功 ====================");
                    log.info("infoMsg:================ 微信异步通知返回的XML格式数据 ====================,xmlString,{}",
                            xmlString.toString());
                    Map<String, String> return_map = WXPayUtil.xmlToMap(xmlString);
                    log.info("infoMsg:================ 微信异步通知返回的XML格式数据转为Map ====================,return_map,{}",
                            JSON.toJSONString(return_map));
                    log.info("infoMsg:--- appKey校验成功");
                    if (return_map.get("return_code").equals("SUCCESS")) {
                        // 账变，修改状态，到账提醒
                        Double amount = Double.parseDouble(return_map.get("total_fee"));
                        String attach = return_map.get("attach");
                        String order_no = return_map.get("out_trade_no");
                        String transaction_id = return_map.get("transaction_id");
                        Double returnAmount = amount / 100;
                        boolean result = rechargeRecordService.updateBillWeiXin(attach, returnAmount, transaction_id, order_no);
                        if (result) {
                            log.info("infoMsg:================ 微信异步通知成功 ====================");
                            wx_map = rechargeRecordService.setXml("SUCCESS", "OK");
                        } else {
                            wx_map = rechargeRecordService.setXml("FAIL", return_map.get("return_msg"));
                        }
                    }
                }
            }
            log.info("infoMsg:================ 微信异步通知请求结束 ====================,wx_map,{}", wx_map);
            return wx_map;
        } catch (Exception e) {
            log.error("errorMsg:{================ 微信异步通知发生异常,异常信息为 msg,{}: ", e.getMessage() + "====================}");
            return rechargeRecordService.setXml("FAIL", e.getMessage());
        } finally {
            try {
                request.getReader().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 微信提现
     *
     * @param transferInfo
     * @return
     */
    @RequestMapping(value = Route.Payment.WX_TRANSFER, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String wx_transfer(HttpServletRequest request, @RequestBody String transferInfo) {
        log.info("infoMsg:--- 用户提现申请开始。======================"
                + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss"));
        JSONObject transferResult = new JSONObject();
        String sign = "";
        try {
            log.info("infoMsg:--- Game服的请求参数 transferInfo,{}======================", JSON.toJSONString(transferInfo));
            // 解析Game服的请求参数 transferInfo
            JSONObject gamePlayerWithDrawalObj = JSON.parseObject(transferInfo);
            Long Id = gamePlayerWithDrawalObj.getLong("Id"); // 流水ID
            Long PlayerId = gamePlayerWithDrawalObj.getLong("PlayerId"); // 用户ID
            String AccountId = gamePlayerWithDrawalObj.getString("AccountId"); // 账号ID
            int ServerId = gamePlayerWithDrawalObj.getInteger("ServerId"); // 服务器ID
            Float Amount = gamePlayerWithDrawalObj.getFloat("Amount"); // 提现金额
            int PayeeType = gamePlayerWithDrawalObj.getInteger("PayeeType"); // 提现类型 1：支付宝 2：微信
            String OpenId = gamePlayerWithDrawalObj.getString("PayeeAccount"); // 用户openid
            String RealUserName = gamePlayerWithDrawalObj.getString("PayeeRealName"); // 真实姓名
            String PassWord = gamePlayerWithDrawalObj.getString("PassWord"); // 用户密码

            // 判断用户的支付密码是否正确
            // boolean validatePwd = appUserService.verifyPassword(AccountId,PassWord);
            // if(!validatePwd) {
            // // 发送服务器的参数
            // JSONObject Obj = new JSONObject();
            // Obj.put("Id", Id);
            // Obj.put("PlayerId", PlayerId);
            // Obj.put("AccountId", AccountId);
            // Obj.put("ServerId", ServerId);
            // Obj.put("Amount", Amount);
            // Obj.put("PayeeType", PayeeType);
            // Obj.put("PayeeAccount", OpenId);
            // Obj.put("PayeeRealName", RealUserName);
            // Obj.put("PassWord", PassWord);
            // Obj.put("OpenId", OpenId);
            // Obj.put("Result", 11001);
            // log.error("errorMsg:--- 提现用户输入的支付密码不匹配 Obj,{}======================" ,
            // JSON.toJSONString(Obj));
            // return JSON.toJSONString(Obj);
            // }
            // paymentServer端 保存提现流水
            String remark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",申请提现" + Amount + "元"
                    + "流水号为：" + Id;
            boolean result = transferRecordService.generatedBills(PlayerId, AccountId, Id, ServerId, Amount,
                    CommonConstant.ALIPAY_WAYTD, OpenId, RealUserName, CommonConstant.Unusual, remark);
            if (!result) {
                log.error("订单生成失败");
            }

            // ----------------请求参数------------------//
            // 准备步骤： 提供要参与签名和要加密的参数
            String App_Key = WxpayConfig.APP_KEY;
            String mch_appid = WxpayConfig.APP_ID; // 应用ID
            String mchid = WxpayConfig.MCH_ID; // 商户ID
            String nonce_str = WXPayUtil.generateNonceStr(); // 随机字符串
            String partner_trade_no = String.valueOf(Id); // 商户订单号
            String openid = OpenId; // 用户openid
            String check_name = "FORCE_CHECK"; // NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
            String re_user_name = RealUserName; // 真实姓名
            int amount = WXRandomNumberUtil.wx_format_PayAmount(String.valueOf(Amount)); // 交易金额
            String desc = "方块创造提现"; // 企业付款描述信息
            String spbill_create_ip = InetAddress.getLocalHost().getHostAddress(); // Ip地址

            // Step2. 拼裝要签名的参数 params
            StringBuffer sb = new StringBuffer();
            sb.append("amount=").append(amount).append("&"); // 应用ID
            sb.append("check_name=").append(check_name).append("&");
            sb.append("desc=").append(desc).append("&");
            sb.append("mch_appid=").append(mch_appid).append("&"); // app_id
            sb.append("mchid=").append(mchid).append("&"); // 商户号
            sb.append("nonce_str=").append(nonce_str).append("&");
            sb.append("openid=").append(openid).append("&"); // 通知地址
            sb.append("partner_trade_no=").append(partner_trade_no).append("&");
            sb.append("re_user_name=").append(re_user_name).append("&");
            sb.append("spbill_create_ip=").append(spbill_create_ip);
            String params = sb.toString();
            log.info("infoMsg:================ 微信提现未拼装key前的签名 ==================== params,{}",
                    JSON.toJSONString(params));

            transferResult.put("Id", Id);
            transferResult.put("PlayerId", PlayerId);
            transferResult.put("AccountId", AccountId);
            transferResult.put("ServerId", ServerId);
            transferResult.put("Amount", Amount);
            transferResult.put("PayeeType", PayeeType);
            transferResult.put("PayeeAccount", openid);
            transferResult.put("PayeeRealName", re_user_name);
            transferResult.put("PassWord", PassWord);

            Query query = new Query();
            query.addCriteria(Criteria.where("serviceId").is("7"));
            query.addCriteria(Criteria.where("serviceName").is("transfer"));
            ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
            if (service.getStatus() == 1) {
                transferResult.put("Result", 11004);
                return JSON.toJSONString(transferResult);
            }

            // 需要签名的数据
            String stringSignTemp = params + "&key=" + App_Key;
            // Step3. MD5加密方式，向微信服务器发送的请求参数
            sign = WXPayUtil.MD5(stringSignTemp).toUpperCase();
            Map<String, String> map = new HashMap<>();
            map.put("amount", String.valueOf(amount));
            map.put("check_name", check_name);
            map.put("desc", desc);
            map.put("mch_appid", mch_appid);
            map.put("mchid", mchid);
            map.put("nonce_str", nonce_str);
            map.put("openid", openid);
            map.put("partner_trade_no", partner_trade_no);
            map.put("re_user_name", re_user_name);
            map.put("sign", sign);
            map.put("spbill_create_ip", spbill_create_ip);

            String url = String.format(WxpayConfig.Transfer_Gateway_Url);
            String xmlstring = WXPayUtil.mapToXml(map);
            log.info("infoMsg:================ 微信统一下单提交的签名 ==================== xmlstring,{}",
                    JSON.toJSONString(xmlstring));
            // Step3. MD5加密方式，向微信服务器发送的请求参数
            List theaderList = new ArrayList<>();
            theaderList.add(new UHeader("Content-Type", "application/x-www-form-urlencoded"));
            // Httpclient发送请求
            // String postResponse = MaryunHttpUtils.getPostResponse(url, xmlstring,
            // theaderList);
            String postResponse = ClientCustomSSL.doRefund(url, xmlstring);
            log.info("infoMsg:================ 微信充值服务器返回的信息 ==================== postResponse,{}",
                    JSON.toJSONString(postResponse));

            // Map<String, String> response = WXPayUtil.xmlToMap(new
            // String(postResponse.getBytes("ISO-8859-1"), "UTF-8"));
            Map<String, String> response = WXPayUtil.xmlToMap(postResponse);

            /**
             * 1. 如果商户重复请求转账，支付宝会幂等返回成功结果，商户必须对重复转账的业务做好幂等处理；如果不判断，存在潜在的风险，商户自行承担因此而产生的所有损失。
             * 2. 如果调用alipay.fund.trans.toaccount.transfer掉单时，
             * 或返回结果code=20000时，或返回结果code=40004，sub_code= SYSTEM_ERROR时，
             * 请调用alipay.fund.trans.order.query发起查询，如果未查询到结果，请保持原请求不变再次请求alipay.fund.trans.toaccount.transfer接口。
             * 3. 商户处理转账结果时，对于错误码的处理，只能使用sub_code作为后续处理的判断依据，不可使用sub_msg作为后续处理的判断依据。
             */
            if (!response.isEmpty() && response.get("return_code").equals("SUCCESS")) {
                log.info("infoMsg:============== 支付宝单笔提现接口调用成功 ===============");
                if (response.get("result_code").equals("SUCCESS")) {
                    // paymentServer执行更改订单状态操作
                    log.info("infoMsg:============== paymentServer执行更改订单状态操作. ===============");
                    // 生成账单
                    String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",提现成功"
                            + Amount + "元" + "流水号为：" + Id;
                    boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
                    if (updateState) {
                        // 发送服务器的参数
                        transferResult.put("Result", 1);
                        log.info("infoMsg:--- 用户提现申请成功。 transferResult,{}======================",
                                JSON.toJSONString(transferResult));
                    }

                } else {
                    if (response.get("err_code").equals("NAME_MISMATCH")) {
                        transferResult.put("Result", 11002);
                        // 生成账单
                        String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                                + ",提现失败。失败原因为：" + response.get("err_code_des") + "提现金额为：" + Amount + "元" + "流水号为："
                                + Id;
                        boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
                        if (updateState) {
                            transferResult.put("Result", 11002);
                            log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================",
                                    JSON.toJSONString(transferResult));
                        }
                    } else {
                        transferResult.put("Result", 11003);
                        // 生成账单
                        String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                                + ",提现失败。失败原因为：" + response.get("err_code_des") + "提现金额为：" + Amount + "元" + "流水号为："
                                + Id;
                        boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
                        if (updateState) {
                            transferResult.put("Result", 11003);
                            log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================",
                                    JSON.toJSONString(transferResult));
                        }
                    }
                }
            }
            log.info("infoMsg:--- 用户提现申请结果。 transferResult,{}======================",
                    JSON.toJSONString(transferResult));
            return JSON.toJSONString(transferResult);
        } catch (Exception e) {
            log.error("errorMsg:{======== 用户申请提现发生异常:======================= " + e.getMessage() + "---}");
            return JSON.toJSONString(transferResult);
        }
    }

}
