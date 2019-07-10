package com.imooc.enums;

import lombok.Getter;

import javax.persistence.Entity;
/**
 * Created By 李世豪
 * 2019-5-22
 * 支付状态
 */

@Getter
public enum PayStatusEnum {

    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功"     )
    ;
    private Integer code;
    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }



}
