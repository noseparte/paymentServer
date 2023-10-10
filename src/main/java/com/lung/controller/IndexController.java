package com.lung.controller;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lung.base.BaseController;
import com.lung.model.AppUser;
import com.lung.service.user.AppUserService;
import com.lung.util.Md5PasswordEncoder;
import com.lung.util.VerifyCodeUtil;
import com.lung.web.api.bean.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@CrossOrigin(maxAge = 3000)
@RequestMapping()
public class IndexController extends BaseController{
	
	@Autowired
	private AppUserService appUserService;

	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/login/userLogin")
	@ResponseBody
	public Response userLogin(HttpServletRequest request,
			@RequestParam(value = "loginId") String loginId,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "veryCode") String veryCode
			) {
	    log.info("infoMsg:=============================== 用户登录开始,loginId,{},password,{},veryCode,{}",loginId,password,veryCode);
	    Response reponse = this.getReponse();
	  	try {
	  	  	// 验证是否登录成功
	  	  	String securityCode = (String) request.getSession().getAttribute("securityCode");
	  	  	if (null == veryCode ||
	  	  		null == securityCode ||
	  	  	    !securityCode.equalsIgnoreCase(veryCode)) {
	  	  		return reponse.failure("验证码错误！");
	  	    }
	  		if (StringUtils.isEmpty(loginId)) {
	  			return reponse.failure("用户名不能为空");
	  		}
	  		if (StringUtils.isEmpty(password)) {
	  			return reponse.failure("密码不能为空");
	  		}
	  		password = Md5PasswordEncoder.encode(password);
	  		Query query = new Query();
	  		query.addCriteria(Criteria.where("userkey").is(loginId));
	  		query.addCriteria(Criteria.where("password").is(password));
	  		AppUser user = appUserService.findOneByQuery(query);
	  		Assert.isTrue(user != null, "用户不存在");
	  		log.info("infoMsg:=============================== 用户登录结束");
	  		return reponse.success();
	  	} catch (Exception e) {
	  		log.info("errorMsg:=============================== 用户登录失败,错误信息:errorMsg,{}",e.getMessage());
	  		return reponse.failure(e.getMessage());
	  	}
	}
	
	/**
	 * send login-security-code
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login/securityCode",method=RequestMethod.GET)
	@ResponseBody
	public Response sendVerifyCode(HttpServletRequest request,HttpServletResponse  response) {
		log.info("web验证码制作开始");
		try {
			// 通知浏览器不要缓存  
			response.setHeader("Expires", "-1");  
			response.setHeader("Cache-Control", "no-cache");  
			response.setHeader("Pragma", "-1");  
			VerifyCodeUtil util = VerifyCodeUtil.Instance();  
			// 将验证码输入到session中，用来验证  
			String code = util.getString();  
			request.getSession().setAttribute("securityCode", code);  
			// 输出到web页面  
			ImageIO.write(util.getImage(), "jpg", response.getOutputStream());  
			log.info("web验证码制作完成");
			return new Response().success();
		} catch (Exception e) {
			log.error("web验证码制作失败",e);
		}
		return new Response().failure();
	}

	
}
