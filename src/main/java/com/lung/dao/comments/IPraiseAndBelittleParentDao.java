package com.lung.dao.comments;

import java.util.List;

import com.lung.model.comments.PraiseAndBelittleParent;
import com.lung.model.comments.PraiseAndBelittleParentDetail;
import com.lung.mongo.dao.GeneralDao;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  IPraiseAndBelittleStory 
 * @创建时间:  2017年12月21日 上午10:08:38
 * @修改时间:  2017年12月21日 上午10:08:38
 * @类说明:
 */
public interface IPraiseAndBelittleParentDao extends GeneralDao<PraiseAndBelittleParent>{

	List<PraiseAndBelittleParent> findByConditions(String comment_parent_id,
			String comment_parent_type);

	PraiseAndBelittleParent create(
			PraiseAndBelittleParentDetail praiseAndBelittleParentDetail);

	PraiseAndBelittleParent updateByPraiseNumAndBelittleNum(
			String commentParentId, String commentParentType,
			Integer praiseCount, Integer belittleCount);

	List<PraiseAndBelittleParent> findByPIdAndPType(String commentParentId,
			String commentParentType);

}
