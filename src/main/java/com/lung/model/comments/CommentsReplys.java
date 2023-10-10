package com.lung.model.comments;

import java.util.Date;
import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsReplys 
 * @创建时间:  2017年12月21日 下午6:57:50
 * @修改时间:  2017年12月21日 下午6:57:50
 * @类说明:故事集评论回复
 */
@Data
public class CommentsReplys {
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
    
    // 回复父id
    private String comments_replys_pid = "0";
    
    // 回复内容
    private String content;
    // 回复玩家id
    private String player_id;
    // 回复玩家名称
    private String player_name;
    // 回复玩家头像
    private String player_img;
    // 回复状态 0 正常 1 删除
    private Integer status = 0;
}