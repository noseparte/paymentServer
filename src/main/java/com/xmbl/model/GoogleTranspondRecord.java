package com.xmbl.model;

import com.xmbl.util.DateUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Copyright © 2018 noseparte © BeiJing BoLuo Network Technology Co. Ltd.
 *
 * @Author Noseparte
 * @Compile --
 * @Version 1.0
 * @Description
 */
@Data
@Document(collection = "pt_google_transpond_record")
public class GoogleTranspondRecord {

    @Id
    @Field("_id")
    private String id;

    @Field("code")
    private String code;

    @Field("access_token")
    private String access_token;

    @Field("refresh_token")
    private String refresh_token;

    @Field("token_type")
    private String token_type;

    @Field("expires_in")
    private Long expires_in;

    private Date createTime = new Date();

    private Date expireTime;

    private int status;

    public GoogleTranspondRecord() {
    }

    public GoogleTranspondRecord(String code, String access_token, String refresh_token, String token_type, Long expires_in, int status) {
        this.code = code;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.expireTime = new Date(System.currentTimeMillis() + expires_in*1000L);
        this.status = status;
    }

}
