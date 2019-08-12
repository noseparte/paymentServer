package com.xmbl.dto;

import java.util.Date;
import java.util.List;

/**
 * @author: sunbenbao
 * @Email: 1402614629@qq.com
 * @类名:  Pagination 
 * @创建时间:  2017年12月21日 下午7:30:57
 * @修改时间:  2017年12月21日 下午7:30:57
 * @类说明:分页数据类 
 */
public class Pagination{
    /** 
     * 一页数据默认20条 
     */  
    private int pageSize = 10;
    
    /** 
     * 当前页码 
     */  
    private int pageNo;
    /** 
     * 一共有多少条数据 
     */  
    private long totalCount; 
    /** 
     * 上一页 
     */  
    private int upPage;  
  
    /** 
     * 下一页 
     */  
    private int nextPage;  
  
  
    /** 
     * 一共有多少页 
     */  
    private int totalPage;  
   
    /** 
     * 数据集合 
     */  
    private List datas;  
  
    /** 
     * 分页的url
     */  
    private String pageUrl;
    
    /**
     * 是否第一页
     */
    private String isFirstPage;
    
    /**
     * 是否最后一页
     */
    private String isLastPage;
  
    /**
     * 当前查询日期
     */
    private Date currentDate;
    
    /** 
     * 获取第一条记录位置 
     *  
     * @return 
     */  
    public int getFirstResult() {  
        return (this.getPageNo() - 1) * this.getPageSize();  
    }  
  
    /** 
     * 获取最后记录位置 
     *  
     * @return 
     */  
    public int getLastResult() {  
        return this.getPageNo() * this.getPageSize();  
    }  
  
    /** 
     * 计算一共多少页 
     */  
    public void setTotalPage() {  
        this.totalPage = (int) ((this.totalCount % this.pageSize > 0) ? (this.totalCount / this.pageSize + 1)  
                : this.totalCount / this.pageSize);  
    }  
  
    /** 
     * 设置 上一页 
     */  
    public void setUpPage() {  
        this.upPage = (this.pageNo > 1) ? this.pageNo - 1 : this.pageNo;  
    }  
  
    /** 
     * 设置下一页 
     */  
    public void setNextPage() {  
        this.nextPage = (this.pageNo == this.totalPage) ? this.pageNo : this.pageNo + 1;  
    }  
  
    public int getNextPage() {  
        return nextPage;  
    }  
  
    public int getTotalPage() {  
        return totalPage;  
    }  
  
    public int getUpPage() {  
        return upPage;  
    }  
  
    public int getPageSize() {  
        return pageSize;  
    }  
  
    public void setPageSize(int pageSize) {  
        this.pageSize = pageSize;  
    }  
  
    public int getPageNo() {  
        return pageNo;  
    }  
  
    public void setPageNo(int pageNo) {  
        this.pageNo = pageNo;  
    }  
  
    public long getTotalCount() {  
        return totalCount;  
    }  
  
    public void setTotalCount(long totalCount2) {  
        this.totalCount = totalCount2;  
    }  
  
    public List getDatas() {  
        return datas;  
    }  
  
    public void setDatas(List datas) {  
        this.datas = datas;  
    }  
  
    public String getPageUrl() {  
        return pageUrl;  
    }  
    
    public String getIsFirstPage() {
		return isFirstPage;
	}

	public void setIsFirstPage(String isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public String getIsLastPage() {
		return isLastPage;
	}

	public void setIsLastPage(String isLastPage) {
		this.isLastPage = isLastPage;
	}

	public void setPageUrl(String pageUrl) {  
        this.pageUrl = pageUrl;  
    }  
  
    public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public Pagination(int pageNo, int pageSize, long totalCount2) {  
        this.setPageNo(pageNo);  
        this.setPageSize(pageSize);  
        this.setTotalCount(totalCount2);  
        this.init();  
    }  
  
    /** 
     * 初始化计算分页 
     */  
    private void init() {  
        this.setTotalPage();// 设置一共页数  
        this.setUpPage();// 设置上一页  
        this.setNextPage();// 设置下一页
        // 设置当前时间
        this.currentDate = new Date();
        // 设置是否第一页
        this.isFirstPage = this.pageNo == 1?"1":"0";
        // 设置是否最后一页
        this.isLastPage = this.pageNo == this.totalPage?"1":"0";
        
    }  
}
