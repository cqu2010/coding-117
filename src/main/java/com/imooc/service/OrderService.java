package com.imooc.service;

import com.imooc.dto.OderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



/**
 * Created By 李世豪
 * 2019-5-23
 * 订单
 */
public interface OrderService {
    /** 创建订单(对象与对象之间涉及到一个转换). */
    OderDTO create(OderDTO oderDTO);

    /** 查询单个订单. */
    OderDTO findOne(String oderId);

    /** 查询订单列表. */
    Page<OderDTO> findList(String buyerOpenid, Pageable pageable);

    /** 取消订单. */
    OderDTO cancel(OderDTO oderDTO);

    /** 完结订单. */
    OderDTO finish(OderDTO oderDTO);

    /** 支付订单. */
    OderDTO paid(OderDTO oderDTO);

}
