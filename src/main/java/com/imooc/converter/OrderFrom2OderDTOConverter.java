package com.imooc.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dto.OderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.from.OrderFrom;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * created By 李世豪
 * 2019-05-29
 * 做了个json转换，把spring转换为对象
 */
@Slf4j
public class OrderFrom2OderDTOConverter {

    public static OderDTO convert(OrderFrom orderForm){
        Gson gson = new Gson();

        OderDTO oderDTO = new OderDTO();

        oderDTO.setBuyerName(orderForm.getName());
        oderDTO.setBuyerPhone(orderForm.getPhone());
        oderDTO.setBuyerAddress(orderForm.getAddress());
        oderDTO.setBuyerOpenid(orderForm.getOpenid());
        /** 做了个json转换，把spring转换为对象 . */
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
           orderDetailList = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("【对象转换】错误, json={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
       oderDTO.setOrderDetailList(orderDetailList);
        return oderDTO;
    }


}
