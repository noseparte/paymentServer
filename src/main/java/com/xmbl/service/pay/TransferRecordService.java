package com.xmbl.service.pay;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.ExtendParams;
import com.xmbl.dao.impl.pay.TransferRecordDaoImpl;
import com.xmbl.dao.pay.TransferRecordDao;
import com.xmbl.model.GameServers;
import com.xmbl.model.TransferRecord;
import com.xmbl.service.GameServersService;
import com.xmbl.util.platformSendYxServerUtil.PlatformSendYouXiServerUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TransferRecordService extends TransferRecordDaoImpl{
	
	@Autowired
	private GameServersService gameServersService;
	@Autowired
	private TransferRecordDao transferRecordDao;

	public boolean generatedBills(Long playerId, String accountId, Long orderNo, int serverId, Float amount, int payeeType,
			String payeeAccount, String payeeRealName, int orderState, String remark) {
		TransferRecord record = new TransferRecord(playerId, accountId, orderNo, serverId, amount, payeeType, payeeAccount, payeeRealName, orderState, remark);
		boolean generated =  transferRecordDao.generatedBills(record);
		if(generated) {
			return true;
		}
		return false;
	}

	public boolean updateBill(String orderNo, String transferRemark) {
		boolean flag = transferRecordDao.updateBill(orderNo,transferRemark);
		if(flag) {
			return true;
		}
		return false;
	}

	/**
	 * 判断目标账户是否满足提现要求
	 * 
	 * @param userKey
	 * @param userAccount
	 * @return
	 */
	public String verifyAccount(String userKey, String userAccount) {
		String result = "";
		try {
			List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match",1);
			Assert.isTrue(gameServersLst!=null && gameServersLst.size() >0 , "match服务不可用，联系管理员添加match服务配置");
			log.info("=================游戏match服务器列表信息:{}",JSONObject.toJSONString(gameServersLst));
			log.info("=================游戏match服务器第一个服务器信息:{}",JSONObject.toJSONString(gameServersLst.get(0)));
			// 发送的服务器地址
			StringBuilder rpcUrl = new StringBuilder();
			// ###
//			 rpcUrl eg http://192.168.0.180:8223/SetChallenageRank
//			rpcUrl.append("http://192.168.0.32:9223");//
			rpcUrl.append("http://");//
			rpcUrl.append(gameServersLst.get(0).getRpcIp())//
				.append(":").append(gameServersLst.get(0).getRpcPort());//
			rpcUrl.append("/SetPlayerAuth");
			log.info("rpcUrl信息:{}", rpcUrl.toString());
			// 发送服务器的参数
			JSONObject Obj = new JSONObject();	
			Obj.put("userKey", userKey);
			Obj.put("userAccount", userAccount);
			String ObjInfo = Obj.toString();
			log.info("像Match服发送的奖励信息为：userKey,{},userAccount,{}",userKey,userAccount);
			String Result = PlatformSendYouXiServerUtil.send(rpcUrl.toString(), ObjInfo);
			if (StringUtils.isNotEmpty(Result)) {
				JSONObject resObj = JSONObject.parseObject(Result);
				result = resObj.getString("Result");
			}
		} catch (Exception e) {
			log.error("error：",e.getMessage());
		}
		return result;
	}

	public String changeAccount(String userKey, String userAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean generatedBills(String orderNo, ExtendParams extendParams) {
		// TODO Auto-generated method stub
		return false;
	}

}
