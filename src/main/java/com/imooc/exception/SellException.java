package com.imooc.exception;

import com.imooc.enums.ResultEnum;

/**
 * created By 李世豪
 * 2019-05-23
 * 异常
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        /** 把message这个内容传到父类的构造方法里 .*/
        super(resultEnum.getMessage());

        this.code=resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code=code;
    }
}
