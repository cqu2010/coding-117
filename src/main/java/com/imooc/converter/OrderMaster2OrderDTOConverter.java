package com.imooc.converter;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;
/**
 * created By 李世豪
 * 2019-05-29
 *
 */
public class OrderMaster2OrderDTOConverter {
    public static OderDTO convert(OrderMaster orderMaster){
        OderDTO oderDTO = new OderDTO();
        BeanUtils.copyProperties(orderMaster ,oderDTO);
        return oderDTO;
    }
    public static List<OderDTO> convert(List<OrderMaster> orderMasterList){
      return orderMasterList.stream().map(e ->
                  convert(e)
                ).collect(Collectors.toList());
    }
}
