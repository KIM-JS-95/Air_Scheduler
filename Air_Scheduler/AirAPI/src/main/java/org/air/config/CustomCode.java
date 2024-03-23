package org.air.config;

import org.air.entity.StatusEnum;

public class CustomCode extends RuntimeException {

    private final StatusEnum status;

    public CustomCode(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

}
