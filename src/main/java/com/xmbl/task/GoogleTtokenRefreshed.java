package com.xmbl.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmbl.config.GoogleConfig;
import com.xmbl.constant.CommonConstant;
import com.xmbl.model.GoogleTranspondRecord;
import com.xmbl.service.pay.GoogleTranspondRecordService;
import com.xmbl.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile --
 * @Version 1.0
 * @Description
 */
@Slf4j
@Configuration
public class GoogleTtokenRefreshed {

    @Autowired
    private GoogleTranspondRecordService googleTranspondRecordService;

    private static String getGoogleAccessToken(String refreshToken) {
        Map<String, String> headers = new HashMap<>();
        Map<String, String> querys = new HashMap<>();
        String result = "";
        try {
            String grant_type = "refresh_token";
            String client_id = GoogleConfig.CLIENT_ID;
            String client_secret = GoogleConfig.CLIENT_SECRET;
            // HTTP 获取access_token
//            https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
            String url = "https://accounts.google.com/o/oauth2/token";
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            headers.put("Accept", "text/plain;charset=utf-8");
            querys.put("client_id", client_id);
            querys.put("client_secret", client_secret);
            querys.put("refresh_token", refreshToken);
            querys.put("grant_type", grant_type);
            result = HttpUtils.send(url, headers, querys, "UTF-8");
            return result;
        } catch (Exception e) {
            log.error("获取access_token失败,errorMsg,{}", e.getMessage());
            return null;
        }
    }

    /**
     * 刷新 Google访问令牌,保证其有效
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    private void tradeAppleQuery() {
        GoogleTranspondRecord lastedRecord = googleTranspondRecordService.findLasted();
        if (lastedRecord != null && lastedRecord.getExpireTime().getTime() > new Date().getTime()+3*60*1000L) {
            log.info("Google的访问令牌还在有效期，无需刷新");
        } else {
            // 1.获取Google刷新令牌
            String refreshToken = lastedRecord.getRefresh_token();
            // HTTP Google服务器 刷新access_token
            String googleAccessToken = getGoogleAccessToken(refreshToken);
            JSONObject tokenObject = JSON.parseObject(googleAccessToken);
            String access_token = tokenObject.getString("access_token");
            String token_type = tokenObject.getString("token_type");
            Long expires_in = tokenObject.getLong("expires_in");
            String refresh_token = tokenObject.getString("refresh_token");
            GoogleTranspondRecord record = new GoogleTranspondRecord(lastedRecord.getCode(), access_token, refresh_token, token_type, expires_in, CommonConstant.Normal);
            // 更新节点
            googleTranspondRecordService.saveRecord(record);
            log.info("Google刷新令牌成功, access_token,{}, 令牌到期时间,expireTime,{}", access_token, record.getExpireTime());
        }

    }


}
