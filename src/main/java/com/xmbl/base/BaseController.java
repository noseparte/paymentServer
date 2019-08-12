package com.xmbl.base;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xmbl.dto.ResponseResult;
import com.xmbl.enumeration.EnumResCode;
import com.xmbl.service.user.AppUserService;
import com.xmbl.web.api.bean.PageData;
import com.xmbl.web.api.bean.Response;

/**
 * Copyright © 2017 noseparte(Libra) © Like the wind, like rain
 *
 * @Author Noseparte
 * @Compile 2017年10月8日 -- 下午3:12:58
 * @Version 1.0
 * @Description
 */
public class BaseController {

	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * 得到PageData
	 */
	public PageData getPageData() {
		try {
			return new PageData(this.getRequest());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	@Autowired
	private AppUserService userService;

	// 系统服务地址
	public String resSuccCode = "200";

	/**
	 * 得到ModelAndView
	 */
	public Response getReponse() {
		return new Response();
	}

	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView() {
		return new ModelAndView();
	}

	public AppUserService getUserService() {
		return userService;
	}

	public void setUserService(AppUserService userService) {
		this.userService = userService;
	}

	/**
	 * 只返回pd
	 *
	 * @param pd
	 * @param pd (应用的名字)
	 * @return
	 */
	@Deprecated
	public Object pd(String uri, PageData pd) {
		// 接收服务器失败抛出的错误
		try {
			pd = this.postForPageData(uri, pd);
		} catch (Exception e) {
			e.printStackTrace();
			pd.put("state", 500);
			pd.put("msg", "服务器错误");
		}
		int state = Integer.parseInt(pd.getString("state"));
		String msg = (String) pd.get("msg");

		if (state != 200) {
			PageData returnPd = new PageData();
			returnPd.put("state", state);
			returnPd.put("msg", msg);
			return returnPd;
		}
		return pd.get("content");

	}

	public PageData postForPageData(String URI, Object param) {
		PageData pd = null;
		try {
			String rest = restTemplate().postForObject(URI, param, String.class);
			pd = JSON.parseObject(rest, PageData.class);

			// PageData pd=JsonUtil.getPageDataFromJsonObjStr(rest);
		} catch (Exception e) {
			e.printStackTrace();
			PageData pageData = new PageData();
			pageData.put("state", 500);
			pageData.put("msg", "服务器错误");
			return pageData;
		}
		return pd;
	}

	/**
	 * 分页封装
	 *
	 * @param list
	 * @return
	 */
	public PageData getPagingPd(List list) {
		PageData pd = new PageData();
		// 分页信息结果集整合到分页框架
		PageInfo pi = new PageInfo(list);
		// 查询结果与分页信息封装为bootstrampTable认可结构
		// pd = BootstrapUtils.parsePage2BootstrmpTable(pi.getTotal(), list);
		if (list == null)
			list = new ArrayList();
		pd.put("total", pi.getTotal());
		pd.put("rows", list);
		return pd;
	}

	protected ResponseResult successJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SUCCESSFUL.value());
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}

	protected ResponseResult successJson(int status, Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(status);
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}

	protected ResponseResult DatatoJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setResult(data);
		return result;
	}

	protected ResponseResult successSaveJson(Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SERVER_SUCCESS.value());
		result.setMsg("ok");
		result.setResult(data);
		return result;
	}

	protected ResponseResult successSaveJson() {
		return successJson(null);
	}

	protected ResponseResult successJson() {
		return successJson(null);
	}

	protected ResponseResult errorJson(int status, String message) {
		ResponseResult result = new ResponseResult();
		result.setStatus(status);
		result.setMsg(message);
		return result;
	}

	protected ResponseResult errorJson(int status, Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatus(status);
		result.setResult(data);
		return result;
	}

	protected ResponseResult errorJson(String message) {
		ResponseResult result = new ResponseResult();
		result.setStatus(EnumResCode.SERVER_ERROR.value());
		result.setMsg(message);
		return result;
	}

}
