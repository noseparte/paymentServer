package com.xmbl.enumeration;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年10月30日 -- 下午3:08:11
 * @Version 1.0
 * @Description		逻辑服通用异常配置文件
 */
public enum EnumInfrastructureResCode {

	/** InfrastructureResCode definition */
	//  SUCCESSFULL
	REGISTER_CODE_SUCCESSFUL		(32201, "32201", "验证码发送成功,请尽快激活,激活码有限期只有30分钟"),

	//  FAILURE	
	GAMESERVER_ERROR				(32500, "32500", "操作有误，请联系管理员"),
	
	//  COMMON
	PARAMETER_FORMAT_ERROR			(32001, "32001", "缺少参数/参数格式不正确"),
	
	//  commentServer
	COMMENT_PARENT_ID_NOT_NULL		(32101, "32101", "评论父对象comment_parent_id不能为空"),  
	COMMENT_PARENT_TYPE_NOT_NULL	(32102, "32102", "评论父对象comment_parent_type不能为空"),  
	PLAYER_ID_NOT_NULL				(32103, "32103", "玩家id不能为空"),  
	PLAYER_NAME_NOT_NULL			(32104, "32104", "玩家姓名不能为空"),  
	PLAYER_IMAGE_NOT_NULL			(32105, "32105", "玩家头像不能为空"),  
	AUTHOR_ID_NOT_NULL				(32106, "32106", "作者id不能为空"),  
	COMMENT_SORT_INCORRENT			(32107, "32107", "sort排序方式传参有误"),  
	COMMENT_CONTENT_NOT_NULL		(32108, "32108", "评论内容不能为空"),  
	COMMENT_TYPE_NOT_EXIST			(32109, "32109", "评论类型不存在"),  
	PLAYERS_HAVE_COMMENTED			(32110, "32110", "玩家已评论过"),  
	COMMENT_CREATION_FAILED			(32111, "32111", "评论创建失败"),  
	COMMENT_ID_NOT_NULL				(32112, "32112", "评论id不能为空"),  
	COMMENT_NOT_EXISTS				(32113, "32113", "该评论已不存在"),  
	
	STORY_ID_NOT_NULL				(32114, "32114", "故事集评论id不能为空"),  
	COMMENTS_THUMB_UP_DELETED		(32115, "32115", "您点赞的评论已被删除"),  
	COMMENTS_THUMB_UP_FAILED		(32116, "32116", "点赞操作失败"),  
	UPDATE_THUMB_UP_NUMBER_FAILED	(32117, "32117", "更新点赞数失败"),  
	COMMENT_CONTENT_REPLY_NOT_NULL	(32118, "32118", "评论回复内容不能为空"),  
	COMMENT_REPLIED_DELETED			(32119, "32119", "您回复的评论已被删除"),  
	COMMENT_REPLIED_FAILED			(32120, "32120", "评论回复失败"),  
	REPLY_OBJECT_NOT_NULL   		(32121, "32121", "回复对象不能为空"),  
	REPLY_ID_NOT_NULL   			(32122, "32122", "回复id不能为空"),  
	COMMENT_REPLIES_NOT_EXIST   	(32123, "32123", "评论回复不存在"),  
	
	THUMB_UP_PLAYER_ID_NOT_NULL     (32124, "32124", "点赞玩家id不能为空"),  
	THUMB_UP_PLAYER_NAME_NOT_NULL   (32125, "32125", "点赞玩家姓名不能为空"),  
	THUMB_UP_PLAYER_IMAGE_NOT_NULL  (32126, "32126", "点赞玩家头像不能为空"),  
	THUMB_UP_TYPE_NOT_NULL    		(32127, "32127", "点赞类型不能为空"),  
	THUMB_UP_TYPE_INCORRENT  		(32128, "32128", "点赞类型 不正确"),  
	THUMB_UP_OBJECT_NOT_NULL  		(32129, "32129", "赞踩评论父对象不能为空"),  
	THUMB_UP_FAILED					(32130, "32130", "赞踩操作异常"),  
	
	FEEDBACK_PLATERID_NOT_NULL		(32131, "32131", "问题反馈玩家id不能为空"),  
	FEEDBACK_PLATER_NAME_NOT_NULL	(32132, "32132", "问题反馈玩家姓名不能为空"),  
	FEEDBACK_DEVICE_TYPE_NOT_NULL	(32133, "32133", "设备类型不能为空"),  
	FEEDBACK_TYPE_NOT_NULL			(32134, "32134", "问题反馈类型不能为空"),  
	APP_VERSION_NOT_NULL			(32135, "32135", "应用版本号不能为空"),  
	FEEDBACK_CONTENT_NOT_NULL		(32136, "32136", "反馈内容不能为空"),  
	FEEDBACK_DEVICE_TYPE_NOT_EXISTS	(32137, "32137", "设备类型不存在"),  
	FEEDBACK_TYPE_NOT_EXISTS		(32138, "32138", "问题反馈类型不存在"),  
	FEEDBACK_FAILURE				(32139, "32139", "问题反馈失败"),  
	
	FEEDBACK_OBJECT_NOT_NULL		(32140, "32140", "问题反馈对象不能为空"),  
	FEEDBACK_REASON_TYPE_NOT_NULL	(32141, "32141", "问题反馈原因类型不能为空"),  
	FEEDBACK_STORAGE_FAILURE		(32142, "32142", "问题反馈信息入库失败"),
	
	REPORT_TYPE_NOT_EXISTS			(32143, "32143", "举报类型不存在"),  
	REPORT_PLAYERID_NOT_NULL		(32144, "32144", "举报玩家id不能为空"),  
	REPORT_PLAYER_NAME_NOT_NULL		(32145, "32145", "举报玩家姓名不能为空"),  
	REPORT_TYPE_NOT_NULL			(32146, "32146", "举报类型不能为空"),  
	REPORT_REASON_TYPE_NOT_NULL		(32147, "32147", "举报原因类型不能为空"),  
	REPORT_REASON_TYPE_NOT_EXISTS	(32148, "32148", "举报原因类型不存在"),  
	CONTENT_HAVE_REPORTED			(32150, "32150", "用户已举报过该内容"),  
	REPORT_STORAGE_FAILURE			(32151, "32151", "举报信息入库失败"),  
	REPORT_OBJECT_NOT_NULL			(32152, "32152", "举报对象不能为空"),  
	
	COMMENT_NODEID_NOT_NULL			(32153, "32153", "节点ID不能为空"),  
	COMMENT_STAGEID_NOT_NULL		(32154, "32154", "小关(关卡)ID不能为空"),  
	COMMENT_PATHTYPE_NOT_NULL		(32155, "32155", "主线/支线不能为空"),  
	COMMENT_NODEINDEX_NOT_NULL		(32156, "32155", "节点索引不能为空"),  
	COMMENT_STAGEINDEX_NOT_NULL		(32157, "32157", "关卡包中关卡的索引不能为空"),  
	//  emailServer
	EMAIL_SEND_FAILURE				(32301, "32301", "发送邮件失败"),  
	
	//  paymentServer
	OBTAIN_PRODUCT_INFO_FAILURE		(32401, "32401", "通过产品id不能获取产品信息"),  
	PLATFORM_INFO_NOT_NULL			(32402, "32402", "商户平台信息不能为空"),  
	APPLE_PAY_INFO_NOT_NULL			(32403, "32403", "苹果内购订单不能为空"),  
	APPLE_PAY_RESPONSE_NOT_NULL		(32405, "32405", "苹果内购订单返回不能为空"),  
	GOOGLE_PLAY_INFO_NOT_NULL		(32406, "32406", "Google内购订单不能为空"),  
	ORDER_HAS_BEEN_SHIPPED			(32407, "32407", "内购订单已发货"),  
	
	//  reportServer
	REGISTER_FAILURE				(32601, "32601", "评论父对象comment_parent_id不能为空"), 
	
	//  smsServer
	APPID_NOT_NULL					(32701, "32701", "appId不能为空"),
	PHONE_NUMBER_INCORRENT			(32702, "32702", "手机号码传参有误"), 
	PHONE_NUMBERS_NOT_NULL			(32703, "32703", "手机号码列表不能为空"), 
	TEMPLATE_ID_NOT_NULL			(32704, "32704", "模板id不能为空"), 
	TEMPLATE_PARAMETERS_NOT_NULL	(32705, "32705", "模板参数不能为空");
	
	private EnumInfrastructureResCode(int status, String resCode, String description) {
		this.status = status;
		this.resCode = resCode;
		this.description = description;
	}
	
	private int status;
	private String resCode;
	private String description;

	public int value() {
		return status;
	}
	public String code() {
		return resCode;
	}
	public String getDescription() {
		return description;
	}
	
}
