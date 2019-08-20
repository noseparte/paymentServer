package com.xmbl.dao.comments;

import com.xmbl.model.comments.CommentType;
import com.xmbl.mongo.dao.GeneralDao;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  IStoryCommentsDao 
 * @创建时间:  2017年12月21日 下午9:05:16
 * @修改时间:  2017年12月21日 下午9:05:16
 * @类说明:
 */
public interface ICommentsTypeDao extends GeneralDao<CommentType>{

	Boolean hasCommentType(String comment_parent_type);

}
