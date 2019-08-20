package com.xmbl.dao.comments;

import java.util.List;

import com.xmbl.model.comments.Comments;
import com.xmbl.mongo.dao.GeneralDao;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  IStoryCommentsDao 
 * @创建时间:  2017年12月21日 下午9:05:16
 * @修改时间:  2017年12月21日 下午9:05:16
 * @类说明:
 */
public interface ICommentsDao extends GeneralDao<Comments>{

	Boolean isExistPlayerComment(String comment_parent_id,
			String comment_parent_type, String player_id, String status);

	Comments insertComments(Comments comments);

	Long allCommentCountByConditions(String comment_parent_id,
			String comment_parent_type, String player_id, String status);

	List<Comments> findCommentLst(String comment_parent_id,
			String comment_parent_type, String player_id, String status,
			String type, String sort, int pageNo, int pageSize);

	Comments findById(String id);

	boolean updatePraiseCountById(String id, Integer changeCount);

	boolean updateReplysCountById(String id, Integer changeCount);

	Comments findByConditions(String comment_parent_id,
			String comment_parent_type, String player_id);

	Long myCountByConditions(String comment_parent_id,
			String comment_parent_type, String player_id, String status);

	List<Comments> findCommentLst(String comment_parent_id, String comment_parent_type, String player_id,
			String status);

	Comments findOneParentCommentOfCommentMaxPraise(String comment_parent_id, String comment_parent_type, String status);
	
	List<Comments> findCommentLsts(String comment_parent_id,
			String comment_parent_type, String player_id, String status,
			String type, String sort, int pageNo, int pageSize);

	Boolean isExistStagePlayerComment(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status);

	Long allStageNodeCommentCountByConditions(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status);

	List<Comments> findStageNodeCommentLsts(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize);

	List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status, String type, String sort, int pageNo, int pageSize);

	List<Comments> findStageNodeCommentLst(String comment_parent_id, String comment_parent_type, String nodeId,
			String stageId, String player_id, String status);

	List<Comments> findStageTreeLst(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize);

	List<Comments> findStageTreeLsts(String comment_parent_id, String player_id,
			String status, String type, String sort, int pageNo, int pageSize);
	
	
	
}
