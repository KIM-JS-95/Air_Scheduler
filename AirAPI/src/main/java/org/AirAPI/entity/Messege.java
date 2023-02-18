package org.AirAPI.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Messege {

    private StatusEnum status;
    private String message;
    private Object data;


}
