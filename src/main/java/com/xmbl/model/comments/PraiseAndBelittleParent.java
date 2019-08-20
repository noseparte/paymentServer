package com.xmbl.model.comments;

import java.util.Date;

import lombok.Data;

/**
 * 
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  PraiseAndBelittleStoryCount 
 * @创建时间:  2017年12月20日 下午9:34:10
 * @修改时间:  2017年12月20日 下午9:34:10
 * @类说明:
 *			 记录某个评论的父对象  点赞和踩的数量
 * 			 备注:   一个评论的父对象的 一条记录
 */
@Data
public class PraiseAndBelittleParent {
	// 评论父对象的点赞或贬低(踩) id
	private String _id;
	// 创建时间
	private Date create_date;
	// 创建人
	private String create_id;
	// 修改时间
	private Date update_date;
	// 修改人
	private String update_id;
	// 评论父对象id
	private String comment_parent_id;
	// 评论父对象名称
	private String comment_parent_name;
	// 评论父对象类型
	private String comment_parent_type;
	// 评论父对象点赞数
	private Integer praise_num = 0;
	// 评论父对象取消赞数
	private Integer belittle_num = 0;
	
}
