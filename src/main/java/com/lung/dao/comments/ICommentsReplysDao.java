package com.lung.dao.comments;

import java.util.List;

import com.lung.model.comments.CommentsReplys;
import com.lung.mongo.dao.GeneralDao;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  IStoryCommentsReplysDao 
 * @创建时间:  2017年12月22日 下午5:57:45
 * @修改时间:  2017年12月22日 下午5:57:45
 * @类说明:
 */
public interface ICommentsReplysDao extends GeneralDao<CommentsReplys>{

	Long allCommentCountByStatus(String comments_id, String status);

	List<CommentsReplys> findCommentReplysLst(String comments_id,String author_id,
			String status, String sort, int pageNo, int pageSize);
	
	List<CommentsReplys> findCommentReplysLsts(String comments_id,String author_id,
			String status, String sort, int pageNo, int pageSize);

	CommentsReplys insertComments(CommentsReplys commentsReplys);
	
	CommentsReplys findOneByCommentsIdAndReplysPid(String comments_id, String comments_replys_pid);
	
	List<CommentsReplys> findByAuthReplys(String comments_id, String authId, String status, String sort, int pageNo,
			int pageSize);
	
	CommentsReplys findOneByCommentsId(String comments_replys_pid);
	
	void removeLstByCommentId(String commentId);
	
	void removeOneByCommentIdAndReplysId(String commentId, String replysId);
	
	CommentsReplys findOneByCommentsIdAndReplysId(String commentId, String replysId);
}
