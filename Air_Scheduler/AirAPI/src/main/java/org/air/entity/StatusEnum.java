package org.air.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    // 20X goot reseponse
    OK("200", "OK"),
    BAD_REQUEST("400", "BAD_REQUEST"),

    /// 00x AWS_ERROR
    CONNECT_ERROR("001", "CONNECT_ERROR"),
    TEXTRACK_ERROR("002", "PARSING_ERROR"),
    TEXTRACK_EMPTY_ERROR("003", "PARSING_EMPTY_ERROR"),
    TEXTRACK_already_save("004", "already_save"),
    /// 10x SQL_GLOBAL_ERROR
    INTERNAL_SERVER_ERROR("101", "INTERNAL_SERVER_ERROR"),

    /// 10x SQL_EXCEPTION_ERROR
    FIND_DATA("102", "SELECT ERROR"),
    SAVE_ERROR("104", "SAVE ERROR"),
    MODIFY_ERROR("105", "MODIFY ERROR"),
    DELETE_ERROR("105", "DELETE ERROR"),

    // 10x SQL_DATA_ERROR
    NOT_FOUND("103", "NOT_FOUND");



    String statusCode;
    String message;

}