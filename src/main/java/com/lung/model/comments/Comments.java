package com.lung.model.comments;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  StoryComments 
 * @创建时间:  2017年12月21日 下午6:32:58
 * @修改时间:  2017年12月21日 下午6:32:58
 * @类说明:评论表
 */
@Data
public class Comments {
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
	// 节点ID
	private String nodeId;
	// 小关(关卡)ID
	private String stageId;
	// 主线/支线
	private String pathType;
	// 节点索引
	private String nodeIndex;
	// 关卡包中关卡的索引
	private String stageIndex;
	// 评论父对象类型
	private String comment_parent_type;
    // 评论人id
    private String player_id;
    // 评论人名称+
    private String player_name;
    // 评论人头像
    private String player_img;
    // 作者id
    private String auther_id;
    // 故事评论内容
    private String content;
    // 评论状态 0 正常状态 1 删除状态
    private Integer status = 0;
    // 回复数
    private Integer replycnt = 0;
    // 点赞数
    private Integer praisecnt = 0;
}