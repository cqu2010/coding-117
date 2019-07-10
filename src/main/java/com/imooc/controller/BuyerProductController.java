package com.imooc.controller;

import com.imooc.VO.ProductInfoVo;
import com.imooc.VO.ProductVO;
import com.imooc.VO.ResultVO;
import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.ResultVOUtil;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 买家商品
 * Created by李世豪
 */
@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    public ResultVO list(){
        //1.查询所有的上架商品
       List<ProductInfo> productInfoList = productService.findUpAll();
        //2.查询类目（一次性查询）
        //传统方法
       /* List<Integer> categoryTypeList =new ArrayList<>();
        for (ProductInfo productInfo:productInfoList){
        categoryTypeList.add(productInfo.getCategoryType());
        }*/
       //精简方法（Java8,lambda)
        List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList());
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3数据拼装
        List<ProductVO> productVOList =new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList){
             ProductVO productVO=new ProductVO();
             productVO.setCategoryType(productCategory.getCategoryType());
             productVO.setCategoryName(productCategory.getCategoryName());


             /** 从productInfo拷贝到productInfoVo视图之后把他带到list里面去.*/
             List<ProductInfoVo> productInfoVoList =new ArrayList<>();
             for (ProductInfo productInfo : productInfoList){
                 if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                     ProductInfoVo productInfoVo=new ProductInfoVo();
                     /** Spring提供BeanUtils.copyProperties 可以将一个对象里属性的值拷贝到另外一个对象里. */
                     BeanUtils.copyProperties(productInfo , productInfoVo);
                     productInfoVoList.add(productInfoVo);
                 }
                 }
                 productVO.setProductInfoVoList(productInfoVoList);
                 productVOList.add(productVO);
             }







       return ResultVOUtil.success(productVOList);
    }

}
