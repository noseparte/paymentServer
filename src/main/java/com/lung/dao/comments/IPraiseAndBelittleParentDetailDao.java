package com.lung.dao.comments;

import java.util.List;

import com.lung.model.comments.PraiseAndBelittleParentDetail;
import com.lung.mongo.dao.GeneralDao;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  IPraiseAndBelittleStoryDetailDao 
 * @创建时间:  2017年12月21日 上午11:06:47
 * @修改时间:  2017年12月21日 上午11:06:47
 * @类说明:
 */
public interface IPraiseAndBelittleParentDetailDao extends GeneralDao<PraiseAndBelittleParentDetail> {

	List<PraiseAndBelittleParentDetail> findByPIdAndPTypeAndPlayerId(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail);

	PraiseAndBelittleParentDetail create(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail);

	List<PraiseAndBelittleParentDetail> findByConditions(
			String commentParentId, String commentParentType, String playerId);

	List<PraiseAndBelittleParentDetail> findByConditions(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail);

	PraiseAndBelittleParentDetail updateByEntity(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail);

}
