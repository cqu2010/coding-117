package com.imooc.service.impl;

import com.imooc.dto.OderDTO;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {

    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;


    @Test
    public void create() throws Exception{

     OderDTO oderDTO = orderService.findOne("1558814701084318188");
     payService.create(oderDTO);

    }

}