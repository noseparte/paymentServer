package com.xmbl.model.problem;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  FeedbackProblem 
 * @创建时间:  2018年5月11日 上午10:41:13
 * @修改时间:  2018年5月11日 上午10:41:13
 * @类说明:
 */
@Data
@Document(collection="feedback_problem")
public class FeedbackProblem {
	// 创建id
	@Id
	private String id;
	// 创建人
	@CreatedBy
	private String create_by;
	// 创建时间
	@CreatedDate
	private Date create_date;
	// 修改人
	@LastModifiedBy
	private String update_by;
	// 修改时间
	@LastModifiedDate
	private Date update_date;
	// 玩家id
	private String player_id;
	// 玩家姓名
	private String player_name;
	// 玩家头像
	private String player_img;
	// 设备类型 1 android  2 ios  3 pc
	private Integer device_type;
	// 应用版本号
	private String app_version;
	// 问题类型 1 游戏奔溃 2 无法战斗 3 数据异常 4 体验建议
	private Integer feedback_type;
	// 问题内容 
	private String feedback_content;
}
