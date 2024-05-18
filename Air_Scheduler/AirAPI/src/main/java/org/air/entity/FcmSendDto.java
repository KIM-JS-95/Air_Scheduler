package org.air.entity;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmSendDto {
    private String token;

    private String title;

    private String body;

}