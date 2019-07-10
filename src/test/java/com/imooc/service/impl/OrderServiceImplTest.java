package com.imooc.service.impl;

import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {
    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID = "110110";

    private final String ORDER_ID ="1558814701084318188";

    @Test
    public void create() throws Exception {
        OderDTO oderDTO = new OderDTO();
        oderDTO.setBuyerName("师兄");
        oderDTO.setBuyerAddress("慕课网");
        oderDTO.setBuyerPhone("123456789123");
        oderDTO.setBuyerOpenid(BUYER_OPENID);
        //购物车
        List<OrderDetail> orderDetailList =new ArrayList<>();
        OrderDetail o1 =new OrderDetail();
        o1.setProductId("12345678");
        o1.setProductQuantity(1);
        orderDetailList.add(o1);

        oderDTO.setOrderDetailList(orderDetailList);
        OderDTO result = orderService.create(oderDTO);
        log.info("【创建订单】 result={}" ,result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() throws Exception {
     OderDTO result = orderService.findOne(ORDER_ID);
     log.info("【查询单个订单】result={}" ,result);
     Assert.assertEquals(ORDER_ID,result.getOrderId());

    }

    @Test
    public void findList() throws Exception{
        PageRequest request =new PageRequest(0,2);
        Page<OderDTO> oderDTOPage = orderService.findList(BUYER_OPENID ,request );
        /* getTotalElements获取总共的个数. */
        Assert.assertNotEquals(0,oderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() throws Exception{
        OderDTO oderDTO = orderService.findOne(ORDER_ID);
        OderDTO result = orderService.cancel(oderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());



    }

    @Test
    public void finish() throws Exception {
        OderDTO oderDTO = orderService.findOne(ORDER_ID);
        OderDTO result = orderService.finish(oderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() throws Exception {
        OderDTO orderDTO = orderService.findOne(ORDER_ID);
        OderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
}