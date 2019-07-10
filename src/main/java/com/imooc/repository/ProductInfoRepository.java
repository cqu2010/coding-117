package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * created by 李世豪
 * 2019-5-18
 *  商品
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    /** 查询上架的商品,通过状态（productStatus）来查. */
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
