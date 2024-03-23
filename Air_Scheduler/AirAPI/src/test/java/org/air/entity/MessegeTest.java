package org.air.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MessegeTest {

    @Test
    public void massege(){
        Messege msg = new Messege(StatusEnum.CONNECT_ERROR.getStatusCode(), "123");
        log.info(msg.toString());
    }
}