package org.air.entity.DTO;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TemppilotcodeDAO {
    private String email;
    private String username;
    private String userid;
    private String password;
    private String pilotid;
    private String androidid;
}
