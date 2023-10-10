package com.lung.service.comments;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lung.dao.comments.IPraiseAndBelittleParentDao;
import com.lung.model.comments.PraiseAndBelittleParent;
import com.lung.model.comments.PraiseAndBelittleParentDetail;


/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleStoryService 
 * @创建时间:  2017年12月20日 下午9:49:06
 * @修改时间:  2017年12月20日 下午9:49:06
 * @类说明: 点赞或踩故事集表service
 */
@Service
public class PraiseAndBelittleParentService{
	
	private static Logger LOGGER = LoggerFactory.getLogger(PraiseAndBelittleParentService.class);
	
	@Autowired
	private IPraiseAndBelittleParentDao iPraiseAndBelittleParentDao;
	
	/**
	 * 查询点赞或者踩 或者取消赞 或 取消踩
	 * @param praiseAndBelittleStoryDetail
	 * @return
	 */
	public PraiseAndBelittleParent findOneByConditions(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail) {
		try {
			LOGGER.info("查询故事集，如果没有，则创建一个故事集");
			List<PraiseAndBelittleParent> praiseAndBelittleParentLst = iPraiseAndBelittleParentDao.findByConditions(praiseAndBelittleParentDetail.getComment_parent_id(),praiseAndBelittleParentDetail.getComment_parent_type());
			PraiseAndBelittleParent praiseAndBelittleParent = null; 
			if (praiseAndBelittleParentLst.size() >= 1) {//如果有数据不用创建 没有数据则创建
				praiseAndBelittleParent = praiseAndBelittleParentLst.get(0);
			} else {
				praiseAndBelittleParent = iPraiseAndBelittleParentDao.create(praiseAndBelittleParentDetail);
			}
			LOGGER.info("查询故事集结束");
			return praiseAndBelittleParent;
		} catch(Exception e) {
			LOGGER.error("=============查询故事集错误:"+e.getMessage());
		}
		return null;
	}
	
	public PraiseAndBelittleParent findPraiseAndBelittleCommentParentByCommentParentId(
			String commentParentId, String commentParentType) {
		try {
			LOGGER.info("查询故事集开始");
			List<PraiseAndBelittleParent> praiseAndBelittleParentLst = iPraiseAndBelittleParentDao.findByPIdAndPType(commentParentId, commentParentType);
			PraiseAndBelittleParent praiseAndBelittleParent = null; 
			if (praiseAndBelittleParentLst.size() >= 1) { //如果有数据不用创建 没有数据则创建
				praiseAndBelittleParent = praiseAndBelittleParentLst.get(0);
			}
			LOGGER.info("查询故事集结束");
			return praiseAndBelittleParent;
		} catch(Exception e) {
			LOGGER.error("===============查询故事集错误:"+e.getMessage());
		}
		return null;
	}
	
}
