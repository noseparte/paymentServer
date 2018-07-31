package com.xmbl.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  AppUserAuth 
 * @创建时间:  2018年5月30日 下午4:27:45
 * @修改时间:  2018年5月30日 下午4:27:45
 * @类说明: app 用户授权表
 */
@Data
@Document(collection="app_user_auth")
public class AppUserAuth {
	
	@Id
	private String id;
	// 创建人
	@Field("create_by")
	private String create_by;
	// 创建时间
	@Field("create_date")
	private Date create_date;
	// 修改人
	@Field("update_by")
	private String update_by;
	// 修改时间
	@Field("update_date")
	private Date update_date;
	// app 用户id
	@Field("app_user_id")
	private String app_user_id;
	// app 用户账号id  平台账号
	@Field("app_user_accountid")
	private String app_user_accountid;
	// app 用户玩家id
	@Field("app_user_playerid")
	private String app_user_playerid;
	// app 用户玩家名
	@Field("app_user_playername")
	private String app_user_playername;
	// app授权类型id
	@Field("auth_type_id")
	private String auth_type_id;
	// app 用户授权类型名
	@Field("auth_type_name")
	private String auth_type_name;
	// 开启时间
	@Field("start_date")
	private Date start_date;
	// 结束时间
	@Field("end_date")
	private Date end_date;
	// app 用户授权状态 0 禁用 1开启 2 长久禁用 3 长久开启
	@Field("status")
	private Integer status = 0; 
	@DBRef
	@Field("appUser")
	private AppUser  appUser;
}
