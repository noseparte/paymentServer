package com.lung.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.lung.base.BaseController;
import com.lung.constant.CommonConstant;
import com.lung.dto.ResponseResult;
import com.lung.model.ServiceRepository;
import com.lung.service.ServiceRepesitoryService;
import com.lung.web.api.bean.PageData;
import com.lung.web.api.bean.Route;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 * @Author Noseparte
 * @Compile 2018年9月18日 -- 下午9:07:58
 * @Version 1.0
 * @Description			平台服务管理
 */
@Slf4j
@Controller
@CrossOrigin(origins = "*",maxAge = 3000)
@RequestMapping(value = Route.PATH + Route.Service.PATH)
public class ServiceRepesitoryController extends BaseController{

	@Autowired
	private ServiceRepesitoryService ServiceRepesitoryService;
	
	/**
	 * 获取服务列表
	 * @return
	 */
	@RequestMapping(value = Route.Service.TO_MANAGER_VIEW,method = RequestMethod.GET)
	public ModelAndView toManagerView() {
		log.info("infoMsg====================== 获取服务列表开始");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try {
			Query query = new Query();
			List<ServiceRepository> serviceList = ServiceRepesitoryService.find(query);
			for(ServiceRepository serviceRepository : serviceList) {
				if(serviceRepository.getServiceName().equals("recharge")) {
					pd.put("rechargeState", serviceRepository.getStatus());
				}
				if(serviceRepository.getServiceName().equals("transfer")) {
					pd.put("transferState", serviceRepository.getStatus());
				}
			}
			mv.addObject("pd", pd);
			mv.setViewName("/manager");
			log.info("infoMsg====================== 获取服务列表结束");
			return mv;
		} catch (Exception e) {
			log.error("errorMsg==========================:获取服务列表失败,errorMsg,{}",e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 获取服务列表
	 * @return
	 */
	@RequestMapping(value = Route.Service.SERVICE_LIST,method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult getServiceList() {
		log.info("infoMsg====================== 获取服务列表开始");
		try {
			Query query = new Query();
			List<ServiceRepository> serviceList = ServiceRepesitoryService.find(query);
			
			log.info("infoMsg====================== 获取服务列表结束");
			return successJson(serviceList);
		} catch (Exception e) {
			log.error("errorMsg==========================:获取服务列表失败,errorMsg,{}",e.getMessage());
			return errorJson(e.getMessage());
		}
	}
	
	/**
	 * 获取单个服务
	 * @return
	 */
	@RequestMapping(value = Route.Service.FIND_ONE,method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult findServiceByCondition(
			@RequestParam(value = "serviceId") String serviceId,
			@RequestParam(value = "serviceName") String serviceName
			) {
		log.info("infoMsg====================== 获取单个服务开始,serviceId,{},serviceName,{}",serviceId,serviceName);
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("serviceId").is(serviceId));
			query.addCriteria(Criteria.where("serviceName").is(serviceName));
			ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
			
			log.info("infoMsg====================== 获取单个服务结束");
			return successJson(service);
		} catch (Exception e) {
			log.error("errorMsg==========================:获取单个服务失败,errorMsg,{}",e.getMessage());
			return errorJson(e.getMessage());
		}
	}
	
	
	/**
	 * 新增服务
	 * @return
	 */
	@RequestMapping(value = Route.Service.ADD_SERVICE,method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult addService(
			@RequestParam(value = "serviceId") String serviceId,
			@RequestParam(value = "serviceName") String serviceName,
			@RequestParam(value = "serviceType") String serviceType,
			@RequestParam(value = "status") int status
			) {
		log.info("infoMsg====================== 新增服务开始,serviceId,{},serviceName,{},serviceType,{},status,{}"
				,serviceId,serviceName,serviceType,status);
		try {
			ServiceRepository service = new ServiceRepository(serviceId, serviceName, serviceType, status);
			ServiceRepesitoryService.save(service);
			log.info("infoMsg====================== 新增服务结束");
			return successJson();
		} catch (Exception e) {
			log.error("errorMsg==========================:新增服务失败,errorMsg,{}",e.getMessage());
			return errorJson(e.getMessage());
		}
	}
	
	/**
	 * 打开或关闭服务
	 * @return
	 */
	@RequestMapping(value = Route.Service.CHANGE_SWITCH,method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult addService() {
		PageData pageData = this.getPageData();
		log.info("infoMsg====================== 打开或关闭服务开始,pageData,{}",JSON.toJSONString(pageData));
		try {
			String serviceName = pageData.getString("serviceName");
			int status = Integer.parseInt(pageData.getString("status"));
			serviceName = serviceName.split("-")[0];
			Query query = new Query().addCriteria(Criteria.where("serviceName").is(serviceName));
			status = (status == 1) ? CommonConstant.Unusual : CommonConstant.Normal;
			Update update = new Update().set("status", status);
			ServiceRepesitoryService.updateFirst(query, update);
			ServiceRepository service = ServiceRepesitoryService.findOneByQuery(query);
			log.info("infoMsg====================== 打开或关闭服务结束");
			return successJson(service);
		} catch (Exception e) {
			log.error("errorMsg==========================:打开或关闭服务失败,errorMsg,{}",e.getMessage());
			return errorJson(e.getMessage());
		}
	}
	
	
	
}
