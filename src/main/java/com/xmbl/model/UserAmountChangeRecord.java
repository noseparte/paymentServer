package com.xmbl.model;

import com.xmbl.mongo.pojo.GeneralBean;

/**
 * 
 * Copyright © 2017 Xunxin Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile 2017年12月17日 -- 下午4:22:27
 * @Version 1.0
 * @Description 		用户交易记录
 */
public class UserAmountChangeRecord extends GeneralBean{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1445924997156016358L;
	
	/**
	 * 我的积分  收入明细
            获取方式:
                          登录 手动签到 +1/天
                          第1天1积分连续签到递增最高31积分 如登录中断从1积分开始重新计算
                          答题 +1/题
                          答题TOP10 50/天
               1=100分 2=80分 3=60分
                           其余排名50分
                           志愿者审核Q&A +5/题
                           认证 +50/项
                           转盘游戏 不等
                           分享软件到朋友圈 +10/次
                           回答悬赏问题 +1/题
                           发布问题上线 +2
                           评论被顶为固定答案+50
                           后台添加
              消费明细: 消费方式
                            志愿者审核失误 -10/题
                            被举报 -100/次
                            发送自画像 -50/次
                            查看答题比例 -50/次
                24小时内改扣积分 24小时后每24小时可更改一次
                             更改答案 -50/次
                             转盘游戏不等
                             看共情圈图片 -10/次
                             所有消费活动遵循扣 福利→积分→现金
                             消耗积分、现金类操作有弹窗，并提示下次不再提醒选项
                             以用户最早使用时间为起点，每1小时统计用户操作，展示时段内用户操作内容。
	 */
	
	
	
	public UserAmountChangeRecord() {
        super();
    }
    public UserAmountChangeRecord(String orderNo,String changeType, String direction, Double tansferBefore, Double tansferAmount,
            Double tansferEnd, int userId) {
        super();
        this.orderNo = orderNo;
        this.changeType = changeType;
        this.direction = direction;
        this.tansferBefore = tansferBefore;
        this.tansferAmount = tansferAmount;
        this.tansferEnd = tansferEnd;
        this.userId = userId;
    }
    
    private String 							orderNo;			//订单号
    private String 							changeType;			//哪个 场景，例如：转盘游戏，广场答题等  TODO
    private String							direction;			//income|expend
	private Double 							tansferBefore;		//订单交易前
	private Double 							tansferAmount;		//交易金额
	private Double 							tansferEnd;			//订单交易后
	private int 							userId;				//用户
	
	
	public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getChangeType() {
        return changeType;
    }
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Double getTansferBefore() {
        return tansferBefore;
    }
    public void setTansferBefore(Double tansferBefore) {
        this.tansferBefore = tansferBefore;
    }
    public Double getTansferAmount() {
        return tansferAmount;
    }
    public void setTansferAmount(Double tansferAmount) {
        this.tansferAmount = tansferAmount;
    }
    public Double getTansferEnd() {
        return tansferEnd;
    }
    public void setTansferEnd(Double tansferEnd) {
        this.tansferEnd = tansferEnd;
    }
    public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
