package com.xmbl.model;

import java.util.Date;

import lombok.Data;

@Data
public class GameServers {
	
	//唯一id
	private String _id;
	
	// 唯一服务器id
	private Long serverId;
	
	//ip key
	private String key;
	
	//服务器名称
	private String name;
	
	// 服务器类型 World Match Battle
	private String type;
	
	//服务器对外id
	private String idGen;
	
	// 服务器对外名称
	private String rpcName;//
	
	// 服务器对外ip
	private String rpcIp;//
	
	// 服务器对外port
	private String rpcPort;
	
	// 七层负载均衡前置ip
	private String frontEndIp;
	
	// 七层负载均衡前置port
	private String frontEndPort;
	
	// 数据库连接
	private String dbConnectionString;
	
	// 数据库名称
	private String dbName;
	
	// 白名单
	private String whiteList;
	
	// new 新服 normal 普服
	private String newtype;
	
	// 推荐 繁忙 爆满 维护
	private String state;
	
	// 创建时间
    private Date createTime;
    
    // 创建人
	private Long createAt;
	
	// 修改时间
	private Date updateTime;
	
	// 修改人
	private Long updateAt;
	
	// 开启 1 关闭 0
    private String statusForStr;
    
	// 操作人
	private String operator;
	
	// 操作状态  0 关闭  1开启  -1 删除
	private Integer operatorStatus;
	
	//执行状态  ： 0 成功 1   待发送  2 正在发送  -1 失败  -2 网络超时
	private Integer executionState;
	
	//当前在线人数
	private Long onlineCount;
	
	//注册人数
	private Long registerCount = 0L;
	
	// ??
	private Long index;
	
	// svn 开始时间
    private Date svnStartTime;
	
    // svn 结束时间
	private Date svnEndTime;
	
	//SVN状态(0 为更新完毕或已最新,2更新中,-1 更新失败)
	private Integer svnStatus;
	
	//SVN版本号
	private String svnVersion;
	
	//SVN状态(NULL 为更新完毕或已最新,2更新中，)
	private String svnStatusStr;
	
	//执行状态  ： 0 成功 1   待发送  2 正在发送  -1 失败  -2 网络超时
	private String executionStateStr;
	
	public GameServers() {
		super();
	}
	
    public GameServers(Long serverId, String key,
			String name, String type, String idGen, String rpcName, String rpcIp, String rpcPort,String frontEndIp, String frontEndPort,
			String dbConnectionString, String dbName,String whiteList, String newtype, String state,Integer operator_status, Date create_time, String operator) {
    	this._id = serverId.toString();
    	this.serverId = serverId;
    	this.key = key;
    	this.name = name;
    	this.type = type;
    	this.idGen = idGen;
    	this.rpcName = rpcName;
    	this.rpcIp = rpcIp;
    	this.rpcPort = rpcPort;
    	this.frontEndIp = frontEndIp;
    	this.frontEndPort = frontEndPort;
    	this.dbConnectionString = dbConnectionString;
    	this.dbName = dbName;
    	this.whiteList = whiteList;
    	this.newtype = newtype;
    	this.state = state;
    	this.operatorStatus = operator_status;
    	this.createTime= create_time;
		this.createAt= create_time.getTime();
    	this.updateTime = new Date();
    	this.updateAt = System.currentTimeMillis();
    	this.operator = operator;
    }
    
    public GameServers(String id, Long serverId, String key,
			String name, String type, String idGen, String rpcName, String rpcIp, String rpcPort,String frontEndIp, String frontEndPort,
			String dbConnectionString, String dbName,String whiteList, String newtype, String state,Integer operator_status, Date create_time, String operator) {
    	this._id = id;
    	this.serverId = serverId;
    	this.key = key;
    	this.name = name;
    	this.type = type;
    	this.idGen = idGen;
    	this.rpcName = rpcName;
    	this.rpcIp = rpcIp;
    	this.rpcPort = rpcPort;
    	this.frontEndIp = frontEndIp;
    	this.frontEndPort = frontEndPort;
    	this.dbConnectionString = dbConnectionString;
    	this.dbName = dbName;
    	this.whiteList = whiteList;
    	this.newtype = newtype;
    	this.state = state;
    	this.operatorStatus = operator_status;
    	this.createTime= create_time;
		this.createAt= create_time.getTime();
    	this.updateTime = new Date();
    	this.updateAt = System.currentTimeMillis();
    	this.operator = operator;
    }
    
    public GameServers(String id, Integer operatorStatus, String operator) {
    	this._id = id;
    	this.operatorStatus = operatorStatus;
    	this.updateTime = new Date();
    	this.updateAt = System.currentTimeMillis();
    	this.operator = operator;
    }
	
	
}
