package com.xmbl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.model.GameServers;
import com.xmbl.service.GameServersService;
import com.xmbl.web.api.bean.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequestMapping
public class GameServersController {

    @Autowired
    private GameServersService gameServersService;

    /**
     * 获取服务器列表
     *
     * @return
     */
    @RequestMapping(value = "/serverList", method = RequestMethod.POST)
    @ResponseBody
    public Response getServerList() {
        log.info("infoMSg:=======================,获取服务器列表开始");
        try {
            List<GameServers> gameServersLst = gameServersService.getServerListByTypeAndOperatorStatus("Match", 1);
            Assert.isTrue(gameServersLst != null && gameServersLst.size() > 0, "match服务不可用，联系管理员添加match服务配置");
            log.info("=================游戏match服务器列表信息:{}", JSONObject.toJSONString(gameServersLst));
            log.info("=================游戏match服务器第一个服务器信息:{}", JSONObject.toJSONString(gameServersLst.get(0)));
            return new Response().success(JSON.toJSONString(gameServersLst));
        } catch (Exception e) {
            log.error("errorMsg:{===================,message,{}", e.getMessage());
            return new Response().failure(e.getMessage());
        }

    }


}
