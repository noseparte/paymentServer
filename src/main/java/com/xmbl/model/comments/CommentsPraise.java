package com.xmbl.model.comments;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsPraise 
 * @创建时间:  2017年12月21日 下午6:49:31
 * @修改时间:  2017年12月21日 下午6:49:31
 * @类说明: 评论点赞表
 */
@Data
public class CommentsPraise {
	// 主键id
    private String _id;
	// 创建人id
    private String create_id;
    // 创建时间
    private Date create_date;
    // 修改人id
    private String update_id;
    // 修改时间
    private Date update_date;
    // 评论父id
    private String comment_parent_id;
	// 评论父对象类型
	private String comment_parent_type;
    // 评论id
    private String comments_id;
    // 点赞玩家id
    private String player_id;
    // 点赞玩家名称
    private String player_name;
    // 点赞玩家头像
    private String player_img;
    // 故事评论点赞状态 0 取消赞 1 点赞
    private Boolean praise_status = Boolean.TRUE;
}