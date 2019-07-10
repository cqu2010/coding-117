package com.imooc.service;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 *Created by 李世豪
 * 2019-5-19 1:58
 */
public interface ProductService {
    /** productId查找一个. */
    ProductInfo findOne(String productId);

    /**
     * C端查询所有的在架商品列表
     * @return
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询所有的管理端商品列表
     * @param pageable 分页
     * @return
     */
    Page<ProductInfo> findAll(Pageable pageable);
    /** 更新添加. */
    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);
}
