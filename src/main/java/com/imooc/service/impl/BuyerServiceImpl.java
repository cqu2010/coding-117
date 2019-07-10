package com.imooc.service.impl;

import com.imooc.dto.OderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by 李世豪
 * 2019-5-20
 */

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {


    @Autowired
    private OrderService orderService;
    @Override
    public OderDTO findOrderOne(String openid, String orderId) {

        return checkOrderOwner(openid , orderId);
    }

    @Override
    public OderDTO cancelOrder(String openid, String orderId) {
        OderDTO oderDTO = checkOrderOwner(openid, orderId);
        if (oderDTO == null) {
            log.error("【取消订单】查不到改订单, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(oderDTO);
    }


    private OderDTO checkOrderOwner(String openid, String orderId){
        OderDTO oderDTO = orderService.findOne(orderId);
        if (oderDTO == null) {
            return null;
        }
        //判断是否是自己的订单
        if (!oderDTO.getBuyerOpenid().equalsIgnoreCase(openid)) {
            log.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}", openid, oderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return oderDTO;
    }
}
