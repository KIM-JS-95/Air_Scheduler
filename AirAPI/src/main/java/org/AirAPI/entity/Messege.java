package org.AirAPI.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Messege {

    private StatusEnum status;
    private String message;
    private Object data;


}
