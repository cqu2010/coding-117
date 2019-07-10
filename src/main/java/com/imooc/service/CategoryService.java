package com.imooc.service;

import com.imooc.dataobject.ProductCategory;

import java.util.List;

/**
 *  created by 李世豪
 *   2019-5-18
 *   类目
 */
public interface CategoryService {
    /** 查询一条数据. */
 ProductCategory findOne(Integer categoryId);
   /** 查询所有. */
 List<ProductCategory> findAll();
  /** 买家端类目数据查询. */
 List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
 /** 新增和更新save. */
 ProductCategory save(ProductCategory productCategory);
}
