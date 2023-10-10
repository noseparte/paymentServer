package com.lung.dto.comments;

import java.util.Date;

import lombok.Data;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  StoryCommentsReplysDto 
 * @创建时间:  2017年12月22日 下午6:16:30
 * @修改时间:  2017年12月22日 下午6:16:30
 * @类说明:
 */
@Data
public class CommentsReplysDto {
	// 主键id
    private String _id;
    // 创建时间
    private Date create_date;
    // 评论人id
    private String player_id;
    // 评论人名称
    private String player_name;
    // 评论人头像
    private String player_img;
    // 故事评论内容
    private String content;
}
