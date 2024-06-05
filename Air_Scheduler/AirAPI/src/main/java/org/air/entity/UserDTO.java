package org.air.entity;

import lombok.Data;

@Data
public class UserDTO {

    private String userid;

    private String password;

    private String email;

    private String name;

    private String picUrl;

    private String device_token; // FCM Token

    private String family;

    private int schedule_chk;

    private String randomkey;

    private String androidid;

    private String family_id; // 기장 아이디

}
