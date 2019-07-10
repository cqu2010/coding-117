package com.imooc.repository;

import com.imooc.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Created by 李世豪
 * 2019-06-22
 * 订单Repository(DAO)
 */
public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
     /** 分页跟据buyerOpenid查询单条订单. */
     Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);
}
