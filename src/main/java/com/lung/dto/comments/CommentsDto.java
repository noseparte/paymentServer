package com.lung.dto.comments;

import java.util.Date;
import java.util.List;

import com.lung.util.DateUtils;
import lombok.Data;

/**
 * @author: noseparte
 * @Email: 1402614629@qq.com
 * @类名:  storyCommentsDto 
 * @创建时间:  2017年12月22日 下午12:49:44
 * @修改时间:  2017年12月22日 下午12:49:44
 * @类说明:
 */
@Data
public class CommentsDto {
	// 主键id
    private String _id;
    // 创建时间
    private Date create_date;
    // 评论父id
    private String comment_parent_id;
	// 评论父对象类型
	private String comment_parent_type;
    // 节点ID
 	private String nodeId;
 	// 小关(关卡)ID
 	private String stageId;
 	//主线/支线
 	private String pathType;
 	//节点索引
 	private String nodeIndex; 							
 	//关卡包中关卡索引
 	private String stageIndex; 	
    // 评论人id
    private String player_id;
    // 评论人名称
    private String player_name;
    // 评论人头像
    private String player_img;
    // 故事评论内容
    private String content;
    // 第三回复人
    private String third_player_name = "";
    // 回复数
    private Integer replycnt = 0;
    // 点赞数
    private Integer praisecnt = 0;
    // 当前查询用户是否给该评论点过赞
    private boolean current_player_ispraise;
    // 是否推荐 0 无状态 1 推荐状态 2 不推荐状态
    private Integer isRecommend = 0;
    // 评论回复list 默认两条 可能有 0 1 2 条
    private List<CommentsReplysDto> commentsReplysDtoLst;

    private String createTick = DateUtils.stampToDate(create_date.getTime());
}
