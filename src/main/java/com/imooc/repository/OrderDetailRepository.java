package com.imooc.repository;

import com.imooc.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 李世豪
 * 2019-06-22
 * 订单详情Repository(DAO)
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail , String> {
    /** 根据Master里面返回的orderId查询订单详情. */
    List<OrderDetail> findByOrderId(String orderId);

}
