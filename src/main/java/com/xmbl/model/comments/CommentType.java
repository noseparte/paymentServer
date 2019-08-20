package com.xmbl.model.comments;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  CommentType 
 * @创建时间:  2018年5月3日 下午6:18:47
 * @修改时间:  2018年5月3日 下午6:18:47
 * @类说明:
 */
@Data
@Document(collection="comments_type")
public class CommentType {
	@Id
	private String id;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	// 评论类别数字标示
	private String numCode;
	// 评论类别单词标示
	private String wordcode;
	// 描述信息
	private String desc;
	// 状态 0 删除 1 开启 2 禁用
	private Integer status = 1;
	// 排序
	private Integer order;
}
