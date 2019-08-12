package com.xmbl.service.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.xmbl.dao.comments.ICommentsPraiseDao;
import com.xmbl.model.comments.CommentsPraise;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsPraiseService 
 * @创建时间:  2017年12月22日 下午1:58:04
 * @修改时间:  2017年12月22日 下午1:58:04
 * @类说明:
 */
@Service
@Repository
public class CommentsPraiseService{

	@Autowired
	private ICommentsPraiseDao iCommentsPraiseDao;
	
	public CommentsPraise findByCommentsIdAndPlayerId(String commentsId,
			String playerId) {
		CommentsPraise commentsPraise = iCommentsPraiseDao.findByConditions(commentsId, playerId);
		return commentsPraise;
	}

	public CommentsPraise addOrRemovePraise(CommentsPraise commentsPraise) {
		commentsPraise = iCommentsPraiseDao.insertOrUpdateCommentsPraise(commentsPraise);
		return commentsPraise;
	}
	
}
