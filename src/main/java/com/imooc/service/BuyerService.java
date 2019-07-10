package com.imooc.service;

import com.imooc.dto.OderDTO;

/**
 * 买家
 * Created By 李世豪
 * 2019-5-31
 */
public interface BuyerService {
    //查询一个订单
    OderDTO findOrderOne(String openid,String orderId);
    //取消订单
    OderDTO cancelOrder(String openid, String orderId);
}
