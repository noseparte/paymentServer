package com.xmbl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.constant.WxpayConfig;
import com.xmbl.service.RechargeRecordService;
import com.xmbl.util.MaryunHttpUtils;
import com.xmbl.util.MaryunHttpUtils.UHeader;
import com.xmbl.util.OrderGeneratedUtils;
import com.xmbl.util.WXPayUtil;
import com.xmbl.util.WXRandomNumberUtil;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import com.xmbl.web.api.bean.Response;
import com.xmbl.web.api.bean.Route;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class WxPayController {

    @Autowired
    private RechargeRecordService rechargeRecordService;

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
    public Response wx_pay(HttpServletRequest request, String jsonData) {
        log.info("infoMsg:================ 微信统一下单请求交易开始 ==================== jsonData,{}", JSON.toJSONString(jsonData));
        String message = "";
        String sign = "";                   //签名
        try {
            // Step1. 解析客户端的请求的订单信息
            JSONObject orderObj = JSON.parseObject(jsonData);
            Long PlayerId = orderObj.getLong("PlayerId");
            String AccountId = orderObj.getString("AccountId");
            String userKey = orderObj.getString("userKey");
            String goodID = orderObj.getString("goodID");
//        	  String Amount = orderObj.getString("Amount");
//        	  String Amount = "1";

            // 通过产品id获取如下信息
            log.info("通过产品id获取如下信息开始:产品id:" + goodID);
            Map<String, String> goodMap = XiaomiGoodsInfoUtil
                    .getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                            "id", goodID);
            log.info("通过产品id获取如下信息:" + goodMap.toString());
            Assert.isTrue(goodMap.size() > 0, "通过产品id不能获取产品信息");
            String goodName = goodMap.get("notice");
            String Amount = goodMap.get("cost");

            // 准备步骤： 提供要参与签名和要加密的参数
            String APP_ID = WxpayConfig.APP_ID;                                        //应用ID
            String App_Key = WxpayConfig.APP_KEY;                                        //应用秘钥
            String mch_Id = WxpayConfig.MCH_ID;                                        //商户ID
            String nonce_str = WXPayUtil.generateNonceStr();                            //随机字符串
            String notify_url = WxpayConfig.Notify_Url;                                //通知地址
            String trade_type = "APP";                                                //签名
            String body = goodName;                                                        //商品描述 ps:腾讯充值中心-QQ会员充值
//        	  String body = "diamond";						              			    	//商品描述 ps:腾讯充值中心-QQ会员充值
//        	  body = new String(body.getBytes("ISO-8859-1"),"UTF-8");
            String out_trade_no = OrderGeneratedUtils.getOrderNo();                    //商户订单号
            int total_fee = WXRandomNumberUtil.wx_format_PayAmount(Amount);            //交易金额
            String spbill_create_ip = InetAddress.getLocalHost().getHostAddress();    //终端IP
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("PlayerId", PlayerId);
            jsonObject.put("AccountId", AccountId);
            jsonObject.put("userKey", userKey);
            jsonObject.put("goodID", goodID);
            String attach = PlayerId + "|" + AccountId + "|" + userKey + "|" +
                    goodID;                                                    //附加数据 ps:用户ID|账号ID|userKey|goodID

            // Step2. 拼裝要签名的参数  params
            StringBuffer sb = new StringBuffer();
            sb.append("appid=").append(APP_ID).append("&");                //应用ID
            sb.append("attach=").append(attach).append("&");
            sb.append("body=").append(body).append("&");
            sb.append("mch_id=").append(mch_Id).append("&");            //商户号
            sb.append("nonce_str=").append(nonce_str).append("&");
            sb.append("notify_url=").append(notify_url).append("&");    //通知地址
            sb.append("out_trade_no=").append(out_trade_no).append("&");
            sb.append("spbill_create_ip=").append(spbill_create_ip).append("&");
            sb.append("total_fee=").append(total_fee).append("&");
            sb.append("trade_type=").append(trade_type);                        //交易类型
            String params = sb.toString();
            log.info("infoMsg:================ 微信统一下单未拼装key前的签名 ==================== params,{}", JSON.toJSONString(params));

            //需要签名的数据
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
            //其实不用单独对body进行编码，只要对组装好的整体xml进行编码就行了：
//      	      xmlstring = new String(xmlstring.getBytes(), "utf-8");
//      	      xmlstring = new String(xmlstring.toString().getBytes("UTF-8"), "ISO-8859-1");
            log.info("infoMsg:================ 微信统一下单提交的签名 ==================== xmlstring,{}", JSON.toJSONString(xmlstring));
            // Step3. MD5加密方式，向微信服务器发送的请求参数
            List theaderList = new ArrayList<>();
            theaderList.add(new UHeader("Content-Type", "application/x-www-form-urlencoded"));
            //Httpclient发送请求
            String postResponse = MaryunHttpUtils.getPostResponse(url, xmlstring, theaderList);
//      	      String postRespons1e = HttpUtil.sendPostUrl(url,String.format(xmlstring));
            log.info("infoMsg:================ 微信充值服务器返回的信息 ==================== postResponse,{}", JSON.toJSONString(postResponse));

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
                        return new Response().success(object);
                    }
                } else {
                    message = response.get("err_code_des");
                    log.error("errorMsg:--- 微信统一下单请求交易解析失败" + message);
                    return new Response().success(message);
                }
            } else {
                log.error("errorMsg:--- 微信统一下单请求交易解析失败" + message);
                return new Response().success(message);
            }
            log.info("infoMsg:================ 微信统一下单请求交易结束 ====================");
            return new Response().success();
        } catch (Exception e) {
            log.error("errorMsg:{================ 微信统一下单请求交易失败:" + e.getMessage() + "====================}");
            return new Response().failure(e.getMessage());
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
    public String wxPayNotify(HttpServletRequest request) {
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
            request.getReader().close();
            if (!StringUtils.isBlank(xmlString)) {
                // Step2. 验证签名环节 微信系统工具类
                /**
                 * WXPayUtil.isSignatureValid(String xmlStr, String key)
                 * @Param xmlString        XML格式数据
                 * @Param key            API密钥
                 */
                boolean isSignature = WXPayUtil.isSignatureValid(xmlString, WxpayConfig.APP_KEY);
                if (isSignature) {
                    log.info("infoMsg:================ 微信异步通知验证签名成功 ====================");
                    log.info("infoMsg:================ 微信异步通知返回的XML格式数据 ====================,xmlString,{}", xmlString.toString());
                    Map<String, String> return_map = WXPayUtil.xmlToMap(xmlString);
                    log.info("infoMsg:================ 微信异步通知返回的XML格式数据转为Map ====================,return_map,{}", JSON.toJSONString(return_map));
                    log.info("infoMsg:--- appKey校验成功");
                    if (return_map.get("return_code").equals("SUCCESS")) {
                        //TODO 账变，修改状态，到账提醒
                        Double amount = Double.parseDouble(return_map.get("total_fee"));
                        String attach = return_map.get("attach");
                        String order_no = return_map.get("out_trade_no");
                        Double returnAmount = amount / 100;
                        boolean result = rechargeRecordService.updateBillWeiXin(attach, returnAmount, order_no);
                        if (result) {
                            log.info("infoMsg:================ 微信异步通知成功 ====================");
                            wx_map = rechargeRecordService.successfully();
                        } else {
                            wx_map = rechargeRecordService.errorfully();
                        }
                    }
                }
            }
            log.info("infoMsg:================ 微信异步通知请求结束 ====================,wx_map,{}", JSON.toJSONString(wx_map));
            return wx_map;
        } catch (Exception e) {
            log.error("errorMsg:{================ 微信异步通知发生异常,异常信息为 msg,{}: ", e.getMessage() + "====================}");
            return rechargeRecordService.errorfully();
        }

    }

}
