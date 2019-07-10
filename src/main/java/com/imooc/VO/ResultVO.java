package com.imooc.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 视图对象
 * Http请求返回的最外层对象
 * Created by李世豪
 * 2019-5-19
 */
@Data
/*//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)/
*//** json序列化子段进行过滤，不序列化null的子段  .*//*
@JsonInclude(JsonInclude.Include.NON_NULL)*/
public class ResultVO<T> {
    /** 错误码. */
    private Integer code;
    /** 提示信息. */
    private String msg="";
    /** 返回的具体内容. */
    private T  data;
}
