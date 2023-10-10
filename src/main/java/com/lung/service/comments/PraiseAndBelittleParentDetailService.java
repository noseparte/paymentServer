package com.lung.service.comments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.lung.dao.comments.IPraiseAndBelittleParentDao;
import com.lung.dao.comments.IPraiseAndBelittleParentDetailDao;
import com.lung.dto.ResponseResult;
import com.lung.enumeration.EnumInfrastructureResCode;
import com.lung.enumeration.EnumResCode;
import com.lung.enumeration.EnumResPraiseOrBelittleCode;
import com.lung.enumeration.EnumResPraiseOrBelittleTypeCode;
import com.lung.model.comments.PraiseAndBelittleParent;
import com.lung.model.comments.PraiseAndBelittleParentDetail;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleStoryDetailService 
 * @创建时间:  2017年12月20日 下午9:49:58
 * @修改时间:  2017年12月20日 下午9:49:58
 * @类说明: 故事集点赞或踩详情表
 */
@Slf4j
@Service
public class PraiseAndBelittleParentDetailService{
	
	@Autowired
	private IPraiseAndBelittleParentDetailDao ipraiseAndBelittleParentDetailDao;
	
	@Autowired
	private IPraiseAndBelittleParentDao ipraiseAndBelittleParentDao;
	
	public PraiseAndBelittleParentDetail findOneByPIdAndPTypeAndPlayerId(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		List<PraiseAndBelittleParentDetail> praiseAndBelittleParentDetailLst = ipraiseAndBelittleParentDetailDao.findByPIdAndPTypeAndPlayerId(praiseAndBelittleParentDetail);
		PraiseAndBelittleParentDetail praiseAndBelittleParentDetail1 = null;
		if (praiseAndBelittleParentDetailLst.size() >= 1) {
			praiseAndBelittleParentDetail1 = praiseAndBelittleParentDetailLst.get(0);
		} else {
			praiseAndBelittleParentDetail1 = ipraiseAndBelittleParentDetailDao.create(praiseAndBelittleParentDetail);
		}
		return praiseAndBelittleParentDetail1;
	}
	
	public ResponseResult updatePraiseAndBelittleParentDetailByPIdAndPTypeAndPlayerId(//
			String commentParentId,//
			String commentParentType,//
			String playerId,//
			String is_praise_or_belittle_type, //
			String type//
	) {
		ResponseResult result = new ResponseResult();
		// 赞踩响应类型
		Integer resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOSTATUS.value();
		try {
			Integer praiseCount = 0;
			Integer belittleCount = 0;
			// 原先既不是赞也不是踩
			if (String.valueOf(EnumResPraiseOrBelittleCode.NOPRAISEBELITTLE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.PRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					belittleCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.BELITTLE.value();
				}
			} else if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOPRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					praiseCount --;
					belittleCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.BELITTLE.value();
				}
			} else if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount ++;
					belittleCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.PRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					belittleCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOBELITTLE.value();
				}
			}
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail = new PraiseAndBelittleParentDetail();
			praiseAndBelittleParentDetail.setComment_parent_id(commentParentId);
			praiseAndBelittleParentDetail.setComment_parent_type(commentParentType);
			praiseAndBelittleParentDetail.setPlayer_id(playerId);
			
			if (resPraiseOrBelittleType.equals(EnumResPraiseOrBelittleTypeCode.NOBELITTLE.value())||resPraiseOrBelittleType.equals(EnumResPraiseOrBelittleTypeCode.NOPRAISE.value())) {// 取消赞或取消踩
				praiseAndBelittleParentDetail.setIs_praise_or_belittle(String.valueOf(EnumResPraiseOrBelittleCode.NOPRAISEBELITTLE.value()));
			} else {
				praiseAndBelittleParentDetail.setIs_praise_or_belittle(type);
			}
			praiseAndBelittleParentDetail = ipraiseAndBelittleParentDetailDao.updateByEntity(praiseAndBelittleParentDetail);
			Assert.isTrue(praiseAndBelittleParentDetail != null, EnumInfrastructureResCode.THUMB_UP_FAILED.code());
			PraiseAndBelittleParent praiseAndBelittleParent = ipraiseAndBelittleParentDao.updateByPraiseNumAndBelittleNum(commentParentId,commentParentType,praiseCount,belittleCount);
			Assert.isTrue(praiseAndBelittleParent != null, EnumInfrastructureResCode.THUMB_UP_FAILED.code());
			result.setStatus(EnumResCode.SUCCESSFUL.value());
			result.setMsg("ok");
			result.setResult(resPraiseOrBelittleType);
			return result;
		} catch (Exception e) {
			log.error("==============报错了，赞踩类型为: "+resPraiseOrBelittleType	+ ",错误信息: "+ e.getMessage());
			result.setStatus(EnumResCode.SERVER_ERROR.value());
			result.setMsg(e.getMessage());
			result.setResult(resPraiseOrBelittleType);
			return result;
		}
	}

	public PraiseAndBelittleParentDetail findOneByConditions(
			String commentParentId,// 评论父对象id
			String commentParentType,// 评论父对象类型
			String playerId// 玩家id
    ) {
		List<PraiseAndBelittleParentDetail> praiseAndBelittleParentDetailLst = ipraiseAndBelittleParentDetailDao.findByConditions(commentParentId,commentParentType,playerId);
		if (praiseAndBelittleParentDetailLst.size() >= 1) {
			return praiseAndBelittleParentDetailLst.get(0);
		} else {
			return null;
		}
	}

	public PraiseAndBelittleParentDetail findOneByConditions(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		List<PraiseAndBelittleParentDetail> praiseAndBelittleParentDetailLst =  ipraiseAndBelittleParentDetailDao.findByConditions(praiseAndBelittleParentDetail);
		PraiseAndBelittleParentDetail praiseAndBelittleParentDetail1 = null;
		if (praiseAndBelittleParentDetailLst.size() >= 1) {
			praiseAndBelittleParentDetail1 = praiseAndBelittleParentDetailLst.get(0);
		} else {
			praiseAndBelittleParentDetail1 = ipraiseAndBelittleParentDetailDao.create(praiseAndBelittleParentDetail);
		}
		return praiseAndBelittleParentDetail1;
	}

	public ResponseResult updateByConditions(//
			String commentParentId,//
			String commentParentType,//
			String playerId,//
			String is_praise_or_belittle_type,//
			String type//
	) {
		ResponseResult result = new ResponseResult();
		// 赞踩响应类型
		Integer resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOSTATUS.value();
		try {
			Integer praiseCount = 0;
			Integer belittleCount = 0;
			// 原先既不是赞也不是踩
			if (String.valueOf(EnumResPraiseOrBelittleCode.NOPRAISEBELITTLE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.PRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					belittleCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.BELITTLE.value();
				}
			} else if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOPRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					praiseCount --;
					belittleCount ++;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.BELITTLE.value();
				}
			} else if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(is_praise_or_belittle_type)) {
				if (String.valueOf(EnumResPraiseOrBelittleCode.PRAISE.value()).equals(type)) {
					praiseCount ++;
					belittleCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.PRAISE.value();
				}
				if (String.valueOf(EnumResPraiseOrBelittleCode.BELITTLE.value()).equals(type)) {
					belittleCount --;
					resPraiseOrBelittleType = EnumResPraiseOrBelittleTypeCode.NOBELITTLE.value();
				}
			}
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail = new PraiseAndBelittleParentDetail();
			praiseAndBelittleParentDetail.setComment_parent_id(commentParentId);
			praiseAndBelittleParentDetail.setComment_parent_type(commentParentType);
			praiseAndBelittleParentDetail.setPlayer_id(playerId);
			if (resPraiseOrBelittleType.equals(EnumResPraiseOrBelittleTypeCode.NOBELITTLE.value())||resPraiseOrBelittleType.equals(EnumResPraiseOrBelittleTypeCode.NOPRAISE.value())) {// 取消赞或取消踩
				praiseAndBelittleParentDetail.setIs_praise_or_belittle(String.valueOf(EnumResPraiseOrBelittleCode.NOPRAISEBELITTLE.value()));
			} else {
				praiseAndBelittleParentDetail.setIs_praise_or_belittle(type);
			}
			praiseAndBelittleParentDetail = ipraiseAndBelittleParentDetailDao.updateByEntity(praiseAndBelittleParentDetail);
			Assert.isTrue(praiseAndBelittleParentDetail != null, EnumInfrastructureResCode.THUMB_UP_FAILED.code());
			PraiseAndBelittleParent praiseAndBelittleParent = ipraiseAndBelittleParentDao.updateByPraiseNumAndBelittleNum(commentParentId,commentParentType, praiseCount,belittleCount);
			Assert.isTrue(praiseAndBelittleParent != null, EnumInfrastructureResCode.THUMB_UP_FAILED.code());
			result.setStatus(EnumResCode.SUCCESSFUL.value());
			result.setMsg("ok");
			result.setResult(resPraiseOrBelittleType);
			return result;
		} catch (Exception e) {
			log.error("===============报错了，赞踩类型为: "+resPraiseOrBelittleType	+ ",错误信息: "+ e.getMessage());
			result.setStatus(EnumResCode.SERVER_ERROR.value());
			result.setMsg(e.getMessage());
			result.setResult(resPraiseOrBelittleType);
			return result;
		}
	}
	
}
