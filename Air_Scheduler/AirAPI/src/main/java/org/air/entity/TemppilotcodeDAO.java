package org.air.entity;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TemppilotcodeDAO {
    private String email;
    private String phonenumber;
    private String username;
    private String userid;
    private String password;
    private String pilotid;
}
