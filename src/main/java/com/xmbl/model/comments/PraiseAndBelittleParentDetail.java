package com.xmbl.model.comments;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleStoryDetail 
 * @创建时间:  2017年12月20日 下午9:33:26
 * @修改时间:  2017年12月20日 下午9:33:26
 * @类说明:
 * 			故事集点赞和踩 明细表
 * 			备注: 点赞或者踩详情 某个故事集的某个人的点赞或者踩记录详情
 */
@Data
public class PraiseAndBelittleParentDetail {
	// 评论父对象明细id
	private String _id;
	// 创建时间
	private Date create_date;
	// 创建人
	private String create_id;
	// 修改时间
	private Date update_date;
	// 修改人
	private String update_id;
	// 评论父对象赞或取消赞id
	private String comment_parent_praise_and_belittle_id;
	// 评论父对象id
	private String comment_parent_id;
	// 评论父对象名称
	private String comment_parent_name;
	// 评论父对象类型
	private String comment_parent_type;
	// 点赞或者踩的玩家id
	private String player_id;
	// 点赞或者踩的玩家名称
	private String player_name;
	// 点赞或者踩的玩家头像
	private String player_img;
	// 是否赞或踩 0 非赞非踩 1 赞 2 踩 
	private String is_praise_or_belittle = "0";
	
}
