package com.imooc.enums;


import lombok.Getter;

/**
 * created by 李世豪
 * 2019-5-18
 *  商品状态
 */
@Getter
public enum ProductStatusEnum {
    UP(0, "在架"),
    DOWN(1 ,"下架")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code,String message) {
        this.code = code;
        this.message=message;
    }
}
