package com.xmbl.model.report;

import java.util.Date;
import lombok.Data;

/**
 *  举报表
 */
@Data
public class Report {
	// 主键
    private String id;
    private String create_id;
    private Date create_date;
    private String update_id;
    private Date update_date;
    // 举报人id
    private String report_player_id;
    // 举报玩家姓名
    private String report_player_name;
    // 举报玩家头像
    private String report_player_img;
    // 举报类型
    private String report_type;
    // 举报对象id
    private String report_obj_id;
    // 举报场景补充信息
    private String report_extend_info;
    // 举报原因类型
    private String report_cause_type;
    // 举报信息描述
    private String report_content;
    // 举报状态 0 举报未处理  1 已处理
    private Integer status = 0;
    // 举报处理结果类型  0 未处理 1 举报证据充足 2 举报证据不足
    private Integer report_result_type = 0;
    
    public Report(//
    		String id, 
    		String report_player_id,
    		String report_player_name,
    		String report_player_img,
    		String report_type,
    		String report_obj_id,
    		String report_extend_info,
    		String report_cause_type,
    		String report_content
    ) {
    	this.id = id;
    	this.create_id = report_player_id;
    	this.create_date = new Date();
    	this.update_id = report_player_id;
    	this.update_date = this.create_date;
    	this.report_player_id = report_player_id;
    	this.report_player_name = report_player_name;
    	this.report_player_img = report_player_img;
    	this.report_type = report_type;
    	this.report_obj_id = report_obj_id;
    	this.report_extend_info = report_extend_info;
    	this.report_cause_type = report_cause_type;
    	this.report_content = report_content;
    	this.status = 0;
    	this.report_result_type = 0;
    }
}