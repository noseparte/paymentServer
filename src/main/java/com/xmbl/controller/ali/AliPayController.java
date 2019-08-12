package com.xmbl.controller.ali;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.xmbl.base.BaseController;
import com.xmbl.constant.AlipayConfig;
import com.xmbl.constant.CommonConstant;
import com.xmbl.model.ThirdPayBean;
import com.xmbl.service.AppUserService;
import com.xmbl.service.BankService;
import com.xmbl.service.ThirdPayService;
import com.xmbl.service.TransferRecordService;
import com.xmbl.util.DateUtils;
import com.xmbl.util.OrderGeneratedUtils;
import com.xmbl.web.api.bean.Response;
import com.xmbl.web.api.bean.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2018年7月2日 -- 下午4:07:36
 * @Version 1.0
 * @Description
 */
@Slf4j
@RestController
@RequestMapping(value = Route.PATH + Route.Payment.PATH)
public class AliPayController extends BaseController {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private BankService bankService;
    @Autowired
    private ThirdPayService thirdPayService;
    @Autowired
    private TransferRecordService transferRecordService;

    /**
     * 支付宝请求交易
     *
     * @param userId
     * @param amount
     * @return
     */
    @RequestMapping(value = Route.Payment.ALI_PAY, method = RequestMethod.POST)
    @ResponseBody
    public Response ali_pay_h(@RequestParam("userId") int userId, @RequestParam("amount") String amount) {
        log.info("infoMsg:--- 支付宝请求交易开始");
        try {
            //----------------请求参数------------------//
            ThirdPayBean payBean = thirdPayService.findByPayId(CommonConstant.ALIPAY_WAYTD);
//			Assert.notNull(payBean);
            String notify_url = payBean.getReturn_url();
            String APP_ID = payBean.getMer_no();    //支付宝商戶号
//			String mer_key = payBean.getMer_key();	//支付宝公钥|商户私钥
            ExtendParams extendParams = new ExtendParams();
            extendParams.setSysServiceProviderId(userId + "|" + 18);    //拓展参数

            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setSubject("循心币");
            String orderNo = OrderGeneratedUtils.getOrderNo();
            model.setOutTradeNo(orderNo);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(amount);
            model.setProductCode("QUICK_WAP_WAY");
            model.setExtendParams(extendParams);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(notify_url);
            // 设置同步地址
            alipay_request.setReturnUrl("www.xunxinkeji.cn://");
            String form = alipayClient.pageExecute(alipay_request).getBody();
            //生成账单
            boolean result = transferRecordService.generatedBills(orderNo, extendParams);
            if (!result) {
                log.error("订单生成失败");
            }
            log.info("infoMsg:--- 支付宝请求交易结束");
            return new Response().success(form);
        } catch (AlipayApiException e) {
            log.error("errorMsg:--- 支付宝请求交易失败" + e.getMessage());
            return new Response().failure(e.getMessage());
        }
    }

    /**
     * 支付宝请求交易
     *
     * @param userId
     * @param amount
     * @return
     */
    @RequestMapping(value = "/ali_pay_h", method = RequestMethod.POST)
    @ResponseBody
    public Response ali_pay_h5(@RequestParam("userId") int userId, @RequestParam("amount") String amount) {
        log.info("infoMsg:--- 支付宝请求交易开始");
        try {
            //----------------请求参数------------------//
//			ThirdPayBean payBean = thirdPayService.findByPayId(18);
//			Assert.notNull(payBean);
            String notify_url = "www.xunxinkeji.cn://api/pay/ali_pay_notify";
            String APP_ID = "wxb927d2d754073085";    //支付宝商戶号
//			String mer_key = payBean.getMer_key();	//支付宝公钥|商户私钥
            String ALIPAY_PUBLIC_KEY = new String("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsbwAZ/90rKQ1MptCdlS96TqYOJa2ypyM4EOjmFYsb2wSgtxNpmWKip6d2Tcobz9jjZvSmalB2RfWBKO82VSyRBLdn3CKdnUtGRMNUN5o3ayElmhWKDzf3LiuP0RWjyXXhMe4ldlXrbYX6ZjnUxmXGLdD1B++yj1hRsZUyScZYdaXb64hxbq4e4GdezGmSE0aRI3ajqOe2DBgbwbJwMKdybp+5iodgc6fag86cYueQ67CpS4BqWyF8rclLvyJUd44VfP1xgxrWpLJVc7gpXsvXDMlTWwM4CPw3OuzwkYuUTDzVGKRenbZRJkFi4FDfKTaBGgiydm39NKs4pJKBQz2KQIDAQAB");
            String APP_PRIVATE_KEY = new String("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCd9ISQRnsfWSWLvEMOsazWbP3J2qtwqm82PKbUggDP4upB+tliPJ1afaswrJRNL7rTW/WEGKbXUOWjfq3RuAJoj0qfxpFhb3HIpZqnkqhyx0u8KQut4DZI13nJ2nK+Ixxbg7zThKQAXfVAr3xx1PhKP5Pceyxz3AcZOzRUxJCzF2YR6TG1qbk8b0cOah9pTtM4tsmKXEo0Uldm+rEb7VBI8Fb7uXp8vunyEGkTgJK4eSZDVkEJ1rdydEy78cj8Ix7oQuO6/nNLfcEtYajiNicTsEOoM2Ane/7S3V1veTCqbVb1agSj7s+lleFP6lcgqReNgk0phf3BpVYnM2XY88epAgMBAAECggEAI5OUXA6T4q6ohz2i+OdJ343y54kJ/jlVDSlCBjE1z5zzWGMQnVC7vEr7yN3GFVB/yuU2ekc3JN4Cqv14VvkUCcrkavJFgmWgginSmJWuvRNoWnwANmx+rY9izfUWzP6Jf48/c4C3k6GWALjF1bm7JrYFLq7Lh1jyfFtaYRFY6g0riaAhPKS/RY1L1tNqzOoS2rge7z9qJJFyDpZM0Lu6pdI9/LvVHS5j6o7WIrqb7gsKgS267uAMFqXwRcyq/P/oTj0wbrlAtgDOfoHAmITbue2jE/oAK18vswefXud5IiWpIKFRRiDNz8UqAUPgmXdEUAVJqgzgTiMHlzKQt7zTZQKBgQDJOLJaLEpRtqGwEhTVzy2r6mnlG29TuapV7Esw3wSqHwx8vLdb0VtgTPq32i1W7BE0p6ZHPkDDOKURRHwCz2as5fh+Udt/YNbTj5nHhudFZ0KULJ0OsPDslx5pe5H1FBPBXPcekzDfwnGOFNOH75gcrAsTiM9XaktOtKRxelRXUwKBgQDI9I10i/UBrezIdo/pz5mjrRSNCRlNVUoMRBnXWeoHqAuAfxxy+TuYVdTelVn8ZgNy7lt+/w9j64XSHMq5S29eJg+FuOKV3HBtiGjxZOz1blEfF3b1pIxNJNAZJPOzpGJNmYQw+rt5s75wXA9n/q/us3HADClMVLFAczKJAt5xkwKBgCHvwvy8TYh8gcZ9NjBdMbm13kg6mUsInDbDlGbYpiO++s8q0M3WgE+8i+hoDo+DXt9/iuanFCsYqZZA851Rt2JfospDKf7QqUqjBG+HTAgDg1IUOCTbKLbuQb3Ojm5EBZTuBeuNLYf/dkFdN9PMT94+EdwojbeTgMH0a2uMEx9rAoGAYW2fv2ezq9LFQBOrhnJuTNq3YgGNUN8O/Y9u7+fZ/UhN+0ilZGDNsfe7Mwc6D5LuDSTfG11R+uHPiaUH7HpUTlMpp22R/ZJYt+Iw7wg9kmifz/EybboPg79bXTV7KheCyZiqbIzDpCevJw6bMZJbfeFmPvQmeal+Hn87ew33Bx0CgYEAkLJ/KSeRbco/jvgzg7G5PHaZbRgrXVmBpfHCsXt/bAqEb0ea2hAG/tJ8yw+8zGG0R8WORZoojWqKfDTjm+0bR+YQxa3HJ/1El9A9jixPgpS+yf9dwBRyCNr2hTepzoaCWAZy5Cialn5FPfSHPJHkNaHPjtxWPihb1s0BzZb3I1s=");
            ExtendParams extendParams = new ExtendParams();
            extendParams.setSysServiceProviderId(userId + "|" + 18);    //拓展参数

            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, APP_ID, APP_PRIVATE_KEY, "JSON", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setSubject("循心币");
            String orderNo = OrderGeneratedUtils.getOrderNo();
            model.setOutTradeNo(orderNo);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(amount);
            model.setProductCode("QUICK_WAP_WAY");
            model.setExtendParams(extendParams);
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(notify_url);
            // 设置同步地址
            alipay_request.setReturnUrl("www.xunxinkeji.cn://");
            String form = alipayClient.pageExecute(alipay_request).getBody();
            //生成账单
//	        boolean result = rechargeRecordService.generatedBills(orderNo,extendParams);
            if (!false) {
                log.error("订单生成失败");
            }
            log.info("infoMsg:--- 支付宝请求交易结束");
            return new Response().success(form);
        } catch (AlipayApiException e) {
            log.error("errorMsg:--- 支付宝请求交易失败" + e.getMessage());
            return new Response().failure(e.getMessage());
        }
    }

    /**
     * 支付宝支付通知地址
     *
     * @param request
     * @return
     */
    @RequestMapping(value = Route.Payment.ALI_PAY_NOTIFY, method = RequestMethod.POST)
    @ResponseBody
    public Response alipay_notify(HttpServletRequest request) {
        log.info("infoMsg:--- 支付宝验证异步通知信息开始");
        //----------------请求参数------------------//
        ThirdPayBean payBean = thirdPayService.findByPayId(CommonConstant.ALIPAY_WAYTD);
        String mer_key = payBean.getMer_key();    //支付宝公钥|商户私钥
        String public_key = mer_key.split("|")[0];
//			String ALIPAY_PUBLIC_KEY = MD5_UTIL.convertMD5(MD5_UTIL.convertMD5(public_key));

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        log.info("infoMsg:--- 支付返回的信息" + requestParams);
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
//				boolean flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "utf-8","RSA2");
        log.info("infoMsg:--- 支付宝信息验证" + true);
        //TODO 账变，修改状态，到账提醒
        Double amount = Double.parseDouble(params.get("total_amount"));
        log.info("infoMsg:--- 支付宝信息验证amount" + amount);
//				String passbackParams = params.get("passbackParams");
        String order_no = params.get("out_trade_no");
        log.info("infoMsg:--- 支付宝信息验证out_trade_no" + order_no);
//				boolean result = transferRecordService.updateBill(amount,order_no);
//					if(result) {
//						RechargeRecord rechargeRecord = rechargeRecordDao.findRechargeRecordByOrderNo(order_no);
//					    UserAmountChangeRecord record = new UserAmountChangeRecord();
//					    record.setDirection(ExpConstants.INCOME);
//					    record.setChangeType("");
//					    record.setTansferAmount(amount);
//					    if(rechargeRecord != null && (Integer)rechargeRecord.getUserId() != null){
//						    UserEntity user = appUserService.findById(rechargeRecord.getUserId());
//						    Double order_before = user.getAmount();     //充值前余额
//				            Double order_end = order_before + amount;     //充值后金额
//				            record.setChangeType(AmountConstants.RECHARGE);
//						    record.setTansferBefore(order_before);
//						    record.setTansferEnd(order_end);
//						    record.setUserId(rechargeRecord.getUserId());
//						    userAmountChangeRecordService.save(record);
//						    return resp.success("SUCCESS");
//					    }
//					}
        log.info("infoMsg:--- 支付宝验证异步通知信息结束");
        return new Response().success("SUCCESS");

    }

    /**
     * 支付宝单笔提现
     *
     * @param //Id            //流水ID
     * @param //PlayerId      //用户ID
     * @param //AccountId     //账号ID
     * @param //ServerId      //服务器ID
     * @param //Amount        //提现金额
     * @param //PayeeType     //提现类型		1：支付宝  2：微信
     * @param //PayeeAccount  //支付宝账号
     * @param //PayeeRealName //真实姓名
     * @param //PassWord      //用户密码
     * @return http://www.ugcapp.com:8028/paymentServer/api/pay/transfer
     */
    @RequestMapping(value = Route.Payment.ALI_TRANSFER, method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String transfer(@RequestBody String transferInfo) {
        log.info("infoMsg:--- 用户提现申请开始。======================" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss"));
        JSONObject transferResult = new JSONObject();
        String order_id = "";
        try {
            log.info("infoMsg:--- Game服的请求参数 transferInfo,{}======================", JSON.toJSONString(transferInfo));
            //解析Game服的请求参数 transferInfo
            JSONObject gamePlayerWithDrawalObj = JSON.parseObject(transferInfo);
            Long Id = gamePlayerWithDrawalObj.getLong("Id");                                    //流水ID
            Long PlayerId = gamePlayerWithDrawalObj.getLong("PlayerId");                        //用户ID
            String AccountId = gamePlayerWithDrawalObj.getString("AccountId");                    //账号ID
            int ServerId = gamePlayerWithDrawalObj.getInteger("ServerId");                        //服务器ID
            Float Amount = gamePlayerWithDrawalObj.getFloat("Amount");            //提现金额
            int PayeeType = gamePlayerWithDrawalObj.getInteger("PayeeType");                    //提现类型		1：支付宝  2：微信
            String PayeeAccount = gamePlayerWithDrawalObj.getString("PayeeAccount");            //支付宝账号
            String PayeeRealName = gamePlayerWithDrawalObj.getString("PayeeRealName");            //真实姓名
            String PassWord = gamePlayerWithDrawalObj.getString("PassWord");                    //用户密码
            // 判断用户的支付密码是否正确
            boolean validatePwd = appUserService.verifyPassword(AccountId, PassWord);
            if (!validatePwd) {
                // 发送服务器的参数
                JSONObject Obj = new JSONObject();
                Obj.put("Id", Id);
                Obj.put("PlayerId", PlayerId);
                Obj.put("AccountId", AccountId);
                Obj.put("ServerId", ServerId);
                Obj.put("Amount", Amount);
                Obj.put("PayeeType", PayeeType);
                Obj.put("PayeeAccount", PayeeAccount);
                Obj.put("PayeeRealName", PayeeRealName);
                Obj.put("PassWord", PassWord);
                Obj.put("Result", 11001);
                log.error("errorMsg:--- 提现用户输入的支付密码不匹配 Obj,{}======================", JSON.toJSONString(Obj));
                return JSON.toJSONString(Obj);
            }
            //paymentServer端 保存提现流水
            String remark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",申请提现" + Amount +
                    "元" + "流水号为：" + Id;
            boolean result = transferRecordService.generatedBills(PlayerId, AccountId, Id, ServerId, Amount, CommonConstant.ALIPAY_WAYTD, PayeeAccount,
                    PayeeRealName, CommonConstant.Unusual, remark);
            if (!result) {
                log.error("订单生成失败");
            }

            //----------------请求参数------------------//
            ThirdPayBean payBean = thirdPayService.findByPayId(CommonConstant.ALIPAY_WAYTD);
            Assert.isTrue(payBean != null, "商户平台信息不能为空");
//			String APP_ID = payBean.getMer_no();	//支付宝商戶号
//			String mer_key = payBean.getMer_key();	//支付宝公钥|商户私钥

            //实例化客户端
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
            model.setOutBizNo(String.valueOf(Id));
            model.setPayeeType("ALIPAY_LOGONID");
            model.setPayeeAccount(PayeeAccount);
            model.setPayeeRealName(PayeeRealName);
            model.setAmount(String.valueOf(Amount));
            model.setPayerShowName("方块创造");
            model.setRemark(remark);
            request.setBizModel(model);
            log.info("=================此次提现申请的具体信息为：-------------" + JSON.toJSONString(model.toString()));
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            /**
             *	 1.  如果商户重复请求转账，支付宝会幂等返回成功结果，商户必须对重复转账的业务做好幂等处理；如果不判断，存在潜在的风险，商户自行承担因此而产生的所有损失。
             *	 2.  如果调用alipay.fund.trans.toaccount.transfer掉单时，
             *			或返回结果code=20000时，或返回结果code=40004，sub_code= SYSTEM_ERROR时，
             *			请调用alipay.fund.trans.order.query发起查询，如果未查询到结果，请保持原请求不变再次请求alipay.fund.trans.toaccount.transfer接口。
             *	 3.  商户处理转账结果时，对于错误码的处理，只能使用sub_code作为后续处理的判断依据，不可使用sub_msg作为后续处理的判断依据。
             */
            transferResult.put("Id", Id);
            transferResult.put("PlayerId", PlayerId);
            transferResult.put("AccountId", AccountId);
            transferResult.put("ServerId", ServerId);
            transferResult.put("Amount", Amount);
            transferResult.put("PayeeType", PayeeType);
            transferResult.put("PayeeAccount", PayeeAccount);
            transferResult.put("PayeeRealName", PayeeRealName);
            transferResult.put("PassWord", PassWord);
            if (response.isSuccess()) {
                order_id = response.getOrderId();
                log.info("infoMsg:============== 支付宝单笔提现接口调用成功 ===============,order_id,{}", order_id);
                if (response.getCode().equals("10000")) {
                    //paymentServer执行更改订单状态操作
                    log.info("infoMsg:============== paymentServer执行更改订单状态操作. ===============");
                    //生成账单
                    String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",提现成功" + Amount +
                            "元" + "流水号为：" + Id;
                    boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
                    if (updateState) {
                        // 发送服务器的参数
                        transferResult.put("Result", 1);
                        log.info("infoMsg:--- 用户提现申请成功。 transferResult,{}======================", JSON.toJSONString(transferResult));
                    }
                } else if (response.getCode().equals("20000") || response.getCode().equals("40004") || response.getSubCode().equals("SYSTEM_ERROR")) {
                    AlipayClient alipayQueryClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.APP_PRIVATE_KEY, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.sign_type);
                    AlipayFundTransOrderQueryRequest requestQuery = new AlipayFundTransOrderQueryRequest();
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("{");
                    buffer.append("\"out_biz_no\":").append(Id).append(",");
                    buffer.append("\"order_id\":").append(order_id);
                    buffer.append("}");
                    requestQuery.setBizContent(buffer.toString());
                    AlipayFundTransOrderQueryResponse responseQuery = alipayQueryClient.execute(requestQuery);
                    if (responseQuery.isSuccess()) {
                        log.info("infoMsg:============== 支付宝单笔提现查询接口调用成功 ===============");
                        // 发送服务器的参数
                        if (responseQuery.getCode().equals("10000")) {
                            //生成账单
                            String transferRemark = "方块创造于" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss") + ",提现失败。失败原因为：" + responseQuery.getFailReason() + "提现金额为：" + Amount +
                                    "元" + "流水号为：" + Id;
                            boolean updateState = transferRecordService.updateBill(String.valueOf(Id), transferRemark);
                            if (updateState) {
                                transferResult.put("Result", 11003);
                                log.error("errorMsg:--- 用户提现申请失败。 transferResult,{}======================", JSON.toJSONString(transferResult));
                            }
                        }
                    } else {
                        log.info("infoMsg:============== 支付宝单笔提现查询接口调用失败 ===============");
                    }
                }
            } else {
                log.info("infoMsg:============== 支付宝单笔提现查询接口系统错误 ===============");
                transferResult.put("Result", 11003);
            }
        } catch (Exception e) {
            log.error("errorMsg:{======== 用户申请提现发生异常:======================= " + e.getMessage() + "---}");
        }
        log.info("infoMsg:--- 用户提现申请结果。 transferResult,{}======================", JSON.toJSONString(transferResult));
        return JSON.toJSONString(transferResult);
    }


//	public static void main(String[] args) {
//		try {
//			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.app_id,AlipayConfig.APP_PRIVATE_KEY,AlipayConfig.format,AlipayConfig.charset,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.sign_type);
//			AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
//			AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
//			model.setOutBizNo("100000000000019");
//			model.setPayeeType("ALIPAY_LOGONID");
//			model.setPayeeAccount("17610173685");
//			model.setAmount("0.1");
//			request.setBizModel(model);
//			
//			AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
//			String body = response.getBody();
//			log.info("提现response=================body,{}",body);
//		} catch (AlipayApiException e) {
//			e.printStackTrace();
//		} 
//	}

}
