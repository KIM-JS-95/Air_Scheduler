package org.air.entity;

import lombok.Data;

@Data
public class UserDTO {

    private String pilotcode;

    private String userid;

    private String password;

    private String email;

    private String name;

    private String picUrl;

    private String device_token;

    private String family;

    private int schedule_chk;

    private String randomkey;

    private String type; // USER / FAMILY

}
