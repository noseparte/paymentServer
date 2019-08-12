package com.xmbl.web.api.bean;

/**
 * 路由链接管理类
 */
public class Route {

	public final static String PATH = "/api";
	public final static String APP_PATH = "/app";

	/**
	 * 服务器管理
	 */
	public class Server {
		public final static String PATH = "/server";

	}

	/**
	 * 服务器管理
	 */
	public class OSS {
		public final static String PATH = "/oss";

		public final static String UPLOAD = "/upload";
		public final static String TO_UPLOAD = "/to_upload";
	}

	/**
	 * 平台服务管理
	 */
	public class Service {
		public final static String PATH = "/service";

		public final static String TO_MANAGER_VIEW = "/to_manager_view";
		public final static String SERVICE_LIST = "/service_list";
		public final static String ADD_SERVICE = "/add_service";
		public final static String FIND_ONE = "/find_service";
		public final static String CHANGE_SWITCH = "/change_switch";

	}

	public class Payment {
		public final static String PATH = "/pay";

		public final static String ALI_PAY = "/ali_pay"; // 支付宝下单
		public final static String ALI_PAY_NOTIFY = "/ali_pay_notify"; // 异步通知
		public final static String ALI_TRANSFER = "/ali_transfer"; // 支付宝用户提现申请
		public final static String WX_TRANSFER = "/wx_transfer"; // 微信用户提现申请

		public final static String WX_PAY = "/wx_pay"; // 微信下单
		public final static String WX_PAY_NOTIFY = "/wx_pay_notify"; // 微信异步通知
		public final static String VERIFY_GOOGLE_ORDER = "/verify_google_order"; // 微信异步通知

		public final static String VERIFY_APPLE_ORDER = "/verify_apple_order"; // 微信异步通知
		public final static String GOOGLE_REDIRECT_URL = "/google_redirect_url"; // 微信异步通知
		public final static String GET_GOOGLE_ACCESS_TOKEN = "/get_google_access_token"; // 微信异步通知
	}

	/**
	 * 用户管理i
	 */
	public class User {
		public final static String PATH = "/user";

		public final static String TO_LIST = "/list.html";
		public final static String USER_LIST = "/user_list";
		public final static String USER_DELETE = "/del";
		public final static String YES_OR_NO = "/yesOrNo"; // 禁用 or 启用
	}

	/**
	 * 举报
	 */
	public class Report {
		public final static String PATH = "/report";

		public final static String ADD = "/add"; // 举报新增
	}

	/**
	 * 问题反馈
	 */
	public class Feedback {
		public final static String PATH = "/feedback";

		public final static String ADD = "/add"; // 问题反馈新增
	}

	/**
	 * 评论接口链接
	 */
	public class Comment {
		public final static String PATH = "/comments";
		public final static String PRAISR = "/praise"; // 点赞
		public final static String REPLYS = "/replys"; // 回复

		public final static String LIST = "/list"; // 查询某个模块下所有评论列表
		public final static String COUNT2 = "/count2"; // 查询某个模块下所有评论数
		public final static String STAGE_NODE_COUNT = "/stage_node_count"; // 查询某个模块下所有评论数
		public final static String LIST2 = "/list2"; // 查询某个模块下所有评论列表
		public final static String STAGE_NODE_COMMENTS_LIST = "/stage_node_comments_list"; // 查询某个模块下所有评论列表
		public final static String STAGE_NODE_ALL_COMMENTS = "/stage_node_all_comments"; // 查询某个模块下所有评论列表
		public final static String MYCOMMENT = "/mycomment"; // 查询某个模块下当前玩家的评论
		public final static String MY_STAGE_NODE_COMMENT = "/my_stage_node_comment"; // 查询某个关卡集下小关的评论
		public final static String ADD = "/add"; // 添加评论
		public final static String ADD_STAGE_COMMENT = "/add_stage_comment"; // 添加评论
		public final static String FINDONE = "/findone"; // 根据评论id 查询某一个评论详情
		public final static String DELETECOMMENT = "/deleteComment"; // 根据评论id 删除某条评论
		public final static String DELETEREPLYS = "/deleteReplys"; // 根据评论、回复id 删除某条回复
		public final static String SAVEORCANCEL = "/saveOrCancel"; // 评论点赞和取消赞
		public final static String PRAISRORBELITTLE = "/praiseOrBelittle"; //
		public final static String SEARCHCOUNT = "/searchCount"; // 查询评论父对象赞踩数量
		public final static String PRAISRORBELITTLESTATUS = "/praiseOrBelittleStatus"; // 查询玩家对某个评论父对象的点赞状态
	}

	/**
	 * 短信服务器
	 */
	public class SMS {
		public static final String PATH = "/sms";

		public static final String SEND = "/send";
	}

	/**
	 * 短信服务器
	 */
	public class Email {
		public static final String PATH = "/email";

		public static final String SEND = "/send";
	}

}
