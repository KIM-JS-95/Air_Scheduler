package org.air.config;

import org.air.entity.StatusEnum;
import org.junit.jupiter.api.Test;

class CustomCodeTest {

    @Test
    public void error1(){
        System.out.println(new CustomCode(StatusEnum.BAD_REQUEST).getMessage());
    }

}