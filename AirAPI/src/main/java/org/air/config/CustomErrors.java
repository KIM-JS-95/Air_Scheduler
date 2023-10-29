package org.air.config;

import org.air.entity.StatusEnum;

public class CustomErrors extends RuntimeException {

    private final StatusEnum status;

    public CustomErrors(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getStatus() {
        return status;
    }

}
