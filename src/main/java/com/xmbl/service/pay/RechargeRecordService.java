package com.xmbl.service.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.constant.CommonConstant;
import com.xmbl.constant.GoodsInfoConstant;
import com.xmbl.dao.pay.RechargeRecordDao;
import com.xmbl.enumeration.EnumGameServerResCode;
import com.xmbl.model.GameServers;
import com.xmbl.model.RechargeRecord;
import com.xmbl.service.GameServersService;
import com.xmbl.util.DateUtils;
import com.xmbl.util.platformSendYxServerUtil.PlatformSendYouXiServerUtil;
import com.xmbl.util.xiaomiUtil.XiaomiGoodsInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@Repository
public class RechargeRecordService {

    @Autowired
    private RechargeRecordDao rechargeRecordDao;
    @Autowired
    private GameServersService gameServersService;

    /**
     * 生成微信充值预付单
     *
     * @param attach
     * @param amount
     * @param outTradeNo
     * @return
     */
    public boolean generatedBills(String attach, String amount, String outTradeNo) {
        try {
            log.info("infoMsg:=================== 生成微信充值预付单,attach,{},amount,{},out_trade_no,{}", attach, amount,
                    outTradeNo);
            if (StringUtils.isNotBlank(attach) && !StringUtils.trim(attach).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID
                String[] orderObj = attach.split("\\|");
                Long PlayerId = Long.parseLong(orderObj[0]);
                String AccountId = String.valueOf(orderObj[1]);
                String userKey = String.valueOf(orderObj[2]);
                String goodID = String.valueOf(orderObj[3]);
                int goodNum = Integer.parseInt(orderObj[4]);
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String goodName = map.get("notice");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "微信充值预付： " + amount + "元" + "订单号：" + outTradeNo;
                RechargeRecord rechargeRecord = new RechargeRecord(PlayerId, AccountId, userKey, "", outTradeNo,
                        CommonConstant.WEIXIN_WAYTD, goodID, goodNum, goodName, amount, new Date(), CommonConstant.Unusual,
                        remark);
                rechargeRecordDao.save(rechargeRecord);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 微信订单账变，修改状态，到账提醒
     *
     * @param attach
     * @param returnAmount
     * @param order_no
     * @return
     */
    public boolean updateBillWeiXin(String attach, Double returnAmount, String transaction_id, String order_no) {
        log.info("infoMsg:=================== 微信充值结果,attach,{},returnAmount,{},out_trade_no,{}", attach, returnAmount,
                order_no);
        String result = "";
        try {
            if (StringUtils.isNotBlank(attach) && !StringUtils.trim(attach).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID|goodName|goodNum
                String[] orderObj = attach.split("\\|");
                Long PlayerId = Long.parseLong(orderObj[0]);
                // String AccountId = String.valueOf(orderObj[1]);
                // String userKey = String.valueOf(orderObj[2]);
                String goodID = String.valueOf(orderObj[3]);
                int goodNum = Integer.parseInt((orderObj[4]));
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "微信充值到账： " + returnAmount + "元" + "订单号：" + order_no;
                // 向游戏服务器发送消息 账变,发送奖励邮件
                List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match", 1);
                Assert.isTrue(gameServersLst != null && gameServersLst.size() > 0, "match服务不可用，联系管理员添加match服务配置");
                log.info("=================游戏match服务器列表信息:{}", JSONObject.toJSONString(gameServersLst));
                log.info("=================游戏match服务器第一个服务器信息:{}", JSONObject.toJSONString(gameServersLst.get(0)));
                // 发送的服务器地址
                StringBuilder rpcUrl = new StringBuilder();
                rpcUrl.append("http://");//
                rpcUrl.append(gameServersLst.get(0).getRpcIp())//
                        .append(":").append(gameServersLst.get(0).getRpcPort());//
                rpcUrl.append("/Recharge");
                log.info("rpcUrl信息:{}", rpcUrl.toString());
                // 发送服务器的参数
                JSONObject Obj = new JSONObject();
                Obj.put("Ticket", order_no);// 订单票据--订单id
                Obj.put("PlayerId", PlayerId);// --玩家id
                Obj.put("RechargeId", goodID);// --商品ID
                Obj.put("RechargeNum", goodNum);// --商品数量
                String ObjInfo = Obj.toString();
                log.info("像Match服发送的奖励信息为：Ticket,{},PlayerId,{},RechargeId,{},goodNum,{}", order_no, PlayerId, goodID, goodNum);
                String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
                if (StringUtils.isNotEmpty(Result)) {
                    JSONObject resObj = JSONObject.parseObject(Result);
                    result = resObj.getString("Result");
                }
                if (verifyGameServerResponse(result)) {
                    rechargeRecordDao.updateBill(transaction_id, order_no, remark);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean generatedAliPayBills(String params, String amount, String outTradeNo) {
        try {
            log.info("infoMsg:=================== 生成支付宝充值预付单,params,{},amount,{},out_trade_no,{}", params, amount,
                    outTradeNo);
            if (StringUtils.isNotBlank(params) && !StringUtils.trim(params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID
                JSONObject jsonObj = JSON.parseObject(params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                String AccountId = jsonObj.getString("AccountId");
                String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String goodName = map.get("notice");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "支付宝充值预付： " + amount + "元" + "订单号：" + outTradeNo;
//				RechargeRecord recharge = new RechargeRecord(playerId, accountId, userKey, orderNo, orderType, goodID, goodNum, goodName, amount, orderTime, orderState, remark)
                RechargeRecord rechargeRecord = new RechargeRecord(PlayerId, AccountId, userKey, "", outTradeNo, CommonConstant.ALIPAY_WAYTD, goodID, goodNum, goodName, amount, new Date(), CommonConstant.Unusual, remark);
                rechargeRecordDao.save(rechargeRecord);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAliPayBill(String passback_params, String total_amount, String trade_no, String out_trade_no) {
        log.info("infoMsg:=================== 支付宝充值结果,passback_params,{},total_amount,{},out_trade_no,{}",
                passback_params, total_amount, out_trade_no);
        String result = "";
        try {
            if (StringUtils.isNotBlank(passback_params) && !StringUtils.trim(passback_params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID|goodName|goodNum
                JSONObject jsonObj = JSON.parseObject(passback_params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                // String AccountId = jsonObj.getString("AccountId");
                // String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "支付宝充值到账： " + total_amount + "元" + "订单号：" + out_trade_no;
                // 向游戏服务器发送消息 账变,发送奖励邮件
                List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match", 1);
                Assert.isTrue(gameServersLst != null && gameServersLst.size() > 0, "match服务不可用，联系管理员添加match服务配置");
                log.info("=================游戏match服务器列表信息:{}", JSONObject.toJSONString(gameServersLst));
                log.info("=================游戏match服务器第一个服务器信息:{}", JSONObject.toJSONString(gameServersLst.get(0)));
                // 发送的服务器地址
                StringBuilder rpcUrl = new StringBuilder();
                rpcUrl.append("http://");//
                rpcUrl.append(gameServersLst.get(0).getRpcIp())//
                        .append(":").append(gameServersLst.get(0).getRpcPort());//
                rpcUrl.append("/Recharge");
                log.info("rpcUrl信息:{}", rpcUrl.toString());
                // 发送服务器的参数
                JSONObject Obj = new JSONObject();
                Obj.put("Ticket", out_trade_no);// 订单票据--订单id
                Obj.put("PlayerId", PlayerId);// --玩家id
                Obj.put("RechargeId", goodID);// --商品ID
                Obj.put("RechargeNum", goodNum);// --商品数量
                String ObjInfo = Obj.toString();
                log.info("像Match服发送的奖励信息为：Ticket,{},PlayerId,{}，RechargeId,{}", out_trade_no, PlayerId, goodID);
                String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
                if (StringUtils.isNotEmpty(Result)) {
                    JSONObject resObj = JSONObject.parseObject(Result);
                    result = resObj.getString("Result");
                }
                if (verifyGameServerResponse(result)) {
                    rechargeRecordDao.updateBill(trade_no, out_trade_no, remark);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("更新支付宝账单失败：errorMsg,{}", e.getMessage());
        }
        return false;
    }

    public boolean generatedAppleBills(String params, String amount, String originalOrderId, String outTradeNo) {
        try {
            log.info("infoMsg:=================== 生成苹果充值预付单,params,{},amount,{},out_trade_no,{}", params, amount,
                    outTradeNo);
            if (StringUtils.isNotBlank(params) && !StringUtils.trim(params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID
                JSONObject jsonObj = JSON.parseObject(params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                String AccountId = jsonObj.getString("AccountId");
                String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String goodName = map.get("notice");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "苹果充值预付： " + amount + "元" + "订单号：" + outTradeNo;
//				RechargeRecord recharge = new RechargeRecord(playerId, accountId, userKey, orderNo, orderType, goodID, goodNum, goodName, amount, orderTime, orderState, remark)
                RechargeRecord rechargeRecord = new RechargeRecord(PlayerId, AccountId, userKey, originalOrderId, outTradeNo, CommonConstant.APPLE_WAYTD, goodID, goodNum, goodName, amount, new Date(), CommonConstant.Unusual, remark);
                rechargeRecordDao.save(rechargeRecord);
                return true;
            }
        } catch (Exception e) {
            log.error("苹果内购订单生成失败. errorMsg,{}", e.getMessage());
        }
        return false;
    }

    public boolean updatAppleBill(String passback_params, String total_amount, String originalOrderId, String out_trade_no) {
        log.info("infoMsg:=================== 苹果充值结果,passback_params,{},total_amount,{},out_trade_no,{}",
                passback_params, total_amount, out_trade_no);
        String result = "";
        try {
            if (StringUtils.isNotBlank(passback_params) && !StringUtils.trim(passback_params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID|goodName|goodNum
                JSONObject jsonObj = JSON.parseObject(passback_params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                // String AccountId = jsonObj.getString("AccountId");
                // String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "苹果充值到账： " + total_amount + "元" + "订单号：" + out_trade_no;
                // 向游戏服务器发送消息 账变,发送奖励邮件
                List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match", 1);
                Assert.isTrue(gameServersLst != null && gameServersLst.size() > 0, "match服务不可用，联系管理员添加match服务配置");
                log.info("=================游戏match服务器列表信息:{}", JSONObject.toJSONString(gameServersLst));
                log.info("=================游戏match服务器第一个服务器信息:{}", JSONObject.toJSONString(gameServersLst.get(0)));
                // 发送的服务器地址
                StringBuilder rpcUrl = new StringBuilder();
                rpcUrl.append("http://");//
                rpcUrl.append(gameServersLst.get(0).getRpcIp())//
                        .append(":").append(gameServersLst.get(0).getRpcPort());//
                rpcUrl.append("/Recharge");
                log.info("rpcUrl信息:{}", rpcUrl.toString());
                // 发送服务器的参数
                JSONObject Obj = new JSONObject();
                Obj.put("Ticket", out_trade_no);// 订单票据--订单id
                Obj.put("PlayerId", PlayerId);// --玩家id
                Obj.put("RechargeId", goodID);// --商品ID
                Obj.put("RechargeNum", goodNum);// --商品数量
                String ObjInfo = Obj.toString();
                log.info("向Match服发送的奖励信息为：Ticket,{},PlayerId,{}，RechargeId,{}", out_trade_no, PlayerId, goodID);
                String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
                if (StringUtils.isNotEmpty(Result)) {
                    JSONObject resObj = JSONObject.parseObject(Result);
                    result = resObj.getString("Result");
                }
                if (verifyGameServerResponse(result)) {
                    rechargeRecordDao.updateBill(originalOrderId, out_trade_no, remark);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("更新苹果内购订单失败。 errorMsg,{}", e.getMessage());
        }
        return false;
    }

    public List<RechargeRecord> findAppleUnAccountingOrder() {
        return rechargeRecordDao.findAppleUnAccountingOrder();
    }

    public boolean generatedGoogleBills(String params, String amount, String originalOrderId, String outTradeNo) {
        try {
            log.info("infoMsg:=================== 生成Google充值预付单,params,{},amount,{},out_trade_no,{}", params, amount,
                    outTradeNo);
            if (StringUtils.isNotBlank(params) && !StringUtils.trim(params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID
                JSONObject jsonObj = JSON.parseObject(params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                String AccountId = jsonObj.getString("AccountId");
                String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String goodName = map.get("notice");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "Google充值预付： " + amount + "元" + "订单号：" + outTradeNo;
//				RechargeRecord recharge = new RechargeRecord(playerId, accountId, userKey, orderNo, orderType, goodID, goodNum, goodName, amount, orderTime, orderState, remark)
                RechargeRecord rechargeRecord = new RechargeRecord(PlayerId, AccountId, userKey, originalOrderId, outTradeNo, CommonConstant.GOOGLE_WAYTD, goodID, goodNum, goodName, amount, new Date(), CommonConstant.Unusual, remark);
                rechargeRecordDao.save(rechargeRecord);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateGoogleBill(String passback_params, String total_amount, String originalOrderId, String out_trade_no) {
        log.info("infoMsg:=================== Google充值结果,passback_params,{},total_amount,{},out_trade_no,{}",
                passback_params, total_amount, out_trade_no);
        String result = "";
        try {
            if (StringUtils.isNotBlank(passback_params) && !StringUtils.trim(passback_params).equals("")) {
                // 附加数据attach ps:用户ID|账号ID|userKey|goodID|goodName|goodNum
                JSONObject jsonObj = JSON.parseObject(passback_params);
                Long PlayerId = jsonObj.getLong("PlayerId");
                // String AccountId = jsonObj.getString("AccountId");
                // String userKey = jsonObj.getString("userKey");
                String goodID = jsonObj.getString("goodID");
                int goodNum = jsonObj.getInteger("goodNum");
                // 通过产品id获取如下信息
                log.info("通过产品id获取如下信息开始:产品id:" + goodID);
                Map<String, String> map = XiaomiGoodsInfoUtil.getOneGoodsMapByKeyVal(GoodsInfoConstant.goodsInfoMapLst,
                        "id", goodID);
                log.info("通过产品id获取如下信息:" + map.toString());
                Assert.isTrue(map.size() > 0, "通过产品id不能获取产品信息");
                String remark = "用户：" + PlayerId + "于：" + DateUtils.formatDate(new Date(), "YYYY-mm-dd HH:mm:ss")
                        + "Google充值到账： " + total_amount + "元" + "订单号：" + out_trade_no;
                // 向游戏服务器发送消息 账变,发送奖励邮件
                List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match", 1);
                Assert.isTrue(gameServersLst != null && gameServersLst.size() > 0, "match服务不可用，联系管理员添加match服务配置");
                log.info("=================游戏match服务器列表信息:{}", JSONObject.toJSONString(gameServersLst));
                log.info("=================游戏match服务器第一个服务器信息:{}", JSONObject.toJSONString(gameServersLst.get(0)));
                // 发送的服务器地址
                StringBuilder rpcUrl = new StringBuilder();
                rpcUrl.append("http://");//
                rpcUrl.append(gameServersLst.get(0).getRpcIp())//
                        .append(":").append(gameServersLst.get(0).getRpcPort());//
                rpcUrl.append("/Recharge");
                log.info("rpcUrl信息:{}", rpcUrl.toString());
                // 发送服务器的参数
                JSONObject Obj = new JSONObject();
                Obj.put("Ticket", out_trade_no);// 订单票据--订单id
                Obj.put("PlayerId", PlayerId);// --玩家id
                Obj.put("RechargeId", goodID);// --商品ID
                Obj.put("RechargeNum", goodNum);// --商品数量
                String ObjInfo = Obj.toString();
                log.info("向Match服发送的奖励信息为：Ticket,{},PlayerId,{}，RechargeId,{}", out_trade_no, PlayerId, goodID);
                String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
                if (StringUtils.isNotEmpty(Result)) {
                    JSONObject resObj = JSONObject.parseObject(Result);
                    result = resObj.getString("Result");
                }
                if (verifyGameServerResponse(result)) {
                    rechargeRecordDao.updateBill(originalOrderId, out_trade_no, remark);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("更新Google订单失败。 errorMsg,{}", e.getMessage());
        }
        return false;
    }

    public List<RechargeRecord> findGoogleUnAccountingOrder() {
        return rechargeRecordDao.findGoogleUnAccountingOrder();
    }

    public boolean findRepeatOrderByOriginalOrderId(String originalOrderId,int orderType) {
        return rechargeRecordDao.
                findRepeatOrderByOriginalOrderId(originalOrderId,orderType);
    }

    /**
     * Game服充值 Response
     *
     * @param result
     * @return
     */
    public Boolean verifyGameServerResponse(String result) {
        if (result.equals(EnumGameServerResCode.SUCCESS.value()) || result.equals(EnumGameServerResCode.ORDER_ACCOUNTED.value())) {
            log.info("游戏服务器充值成功: 充值时间 " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss")
                    + ",响应结果 result" + result.toString());
            return true;
        } else {
            log.info("游戏服务器充值失败: 充值时间 " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss")
                    + ",响应结果 result" + result.toString());
            return false;
        }
    }

    /**
     * 通知微信支付系统接收到信息
     *
     * @return
     */
    public String successfully() {
        try {
            // data.put("return_code", "SUCCESS");
            // data.put("return_msg", "OK");
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过xml 发给微信消息
     *
     * @param return_code
     * @param return_msg
     * @return
     */
    public String setXml(String return_code, String return_msg) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("return_code", return_code);
        parameters.put("return_msg", return_msg);
        return "<xml><return_code><![CDATA[" + return_code + "]]>" + "</return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    /**
     * 获取HttpServletRequest的响应值
     * 获取支付宝POST过来反馈信息
     *
     * @param request
     * @return
     */
    public Map<String, String> parseResponseReferToMap(HttpServletRequest request){
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    public void closeStreamsGracelly(PrintWriter out, BufferedReader in){
        if (out != null) {
            out.close();
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
