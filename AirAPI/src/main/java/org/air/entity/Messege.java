package org.air.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Messege {

    private String statuscode;
    private String message;

    public Messege Messege(String statusCode, String messege) {
        return Messege.builder()
                .statuscode(statusCode)
                .message(messege)
                .build();
    }
}
