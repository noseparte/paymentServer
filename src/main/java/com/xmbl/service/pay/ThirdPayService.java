package com.xmbl.service.pay;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.xmbl.dao.impl.pay.ThirdPayDaoImpl;
import com.xmbl.dao.pay.ThirdPayDao;
import com.xmbl.model.ThirdPayBean;

@Repository
public class ThirdPayService extends ThirdPayDaoImpl{

	@Autowired
	private ThirdPayDao thirdPayDao;
	
	/**
	 * 获取第三方支付信息
	 * @param payId
	 * @return
	 */
	public ThirdPayBean findByPayId(int payId) {
		try {
			Query query = new Query();
			if(StringUtils.isNotBlank(String.valueOf(payId)) && !StringUtils.trim(String.valueOf(payId)).equals("")) {
				query.addCriteria(Criteria.where("bank_id").is(payId));
			}
			return thirdPayDao.findOneByQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean generatedBills(Map<String, String> response, String attach, String amount, String out_trade_no) {
		return false;
	}

}
