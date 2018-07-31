package com.xmbl.util.pathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 获取项目路劲工具类
 * 
 * @author sunbenbao
 *
 */
public class ProjectPathUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(ProjectPathUtil.class);
	
	/**
	 * 获取项目根路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		try {
			String rootPath = getProjectPath("/");
			return rootPath;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("获取跟路径报错,报错信息为:"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据相对路劲获取绝对路劲
	 * @param relative
	 * @return
	 */
	public static String getProjectPath(String relativePath) {
		try {
			LOGGER.info("获取路径开始");
			String absolutePath = "";
			if (relativePath == null || "".equals(relativePath.trim())) {
				absolutePath = getRootPath();
			} else {
				if (relativePath.trim().indexOf("/") != 0) {
					relativePath = "/" + relativePath.trim();
				}
				absolutePath =  ProjectPathUtil.class.getResource(relativePath).getPath();
//				absolutePath =  ProjectPathUtil.class.getClassLoader().getResource(relativePath).getPath();
//				absolutePath = ProjectPathUtil.class.getClass().getResource(relativePath).getPath();
			}
			LOGGER.info("获取路径结束");
			return absolutePath;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("获取路径报错,报错信息为:"+e.getMessage());
		}
		return null;
	}
	
	public static void main(String[] args) {
//		System.out.println(getRootPath());
//		System.out.println(getProjectPath("/"));
//		System.out.println(getProjectPath("xml/dev/main.commodityConfig.xml"));
//		System.out.println(getProjectPath("/xml/dev/main.commodityConfig.xml"));
	}
}
