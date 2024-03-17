package org.air.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {

    OK("200", "OK"),

    BAD_REQUEST("400", "BAD_REQUEST"),
    NO_DATA("401","No_Data"),
    NOT_FOUND("404", "NOT_FOUND"),

    INTERNAL_SERER_ERROR("500", "INTERNAL_SERVER_ERROR"),
    SAVE_ERROR("505","SAVE ERROR"),

    DELETE_ERROR("E402","delete fail");


    String statusCode;
    String message;


}