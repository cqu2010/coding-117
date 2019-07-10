package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataobject.OrderDetail;

import com.imooc.utils.serializer.Date2LongSerializer;
import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 李世豪
 * 2019-06-22
 * 订单数据传输S
 */
@Data
/*//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)/
*//** json序列化子段进行过滤，不序列化null的子段  .*//*
@JsonInclude(JsonInclude.Include.NON_NULL)*/
public class OderDTO {
    /** 订单id. */
    private String orderId;
    /** 买家名字. */
    private String buyerName;
    /** 买家手机号. */
    private String buyerPhone;
    /** 买家地址. */
    private String buyerAddress;
    /** 买家微信Openid. */
    private String buyerOpenid;
    /** 订单总金额. */
    private BigDecimal orderAmount;
    /** 订单状态,默认为新订单. */
    private Integer orderStatus;
    /** 支付状态,默认为0未支付. */
    private Integer payStatus;
    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;
    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    private List<OrderDetail> orderDetailList;

}
