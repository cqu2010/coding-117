package com.imooc.controller;

import com.imooc.VO.ResultVO;
import com.imooc.converter.OrderFrom2OderDTOConverter;
import com.imooc.dto.OderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.from.OrderFrom;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import com.imooc.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * created By 李世豪
 * 2019-05-29
 *
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderFrom orderFrom,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", orderFrom);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }


        OderDTO oderDTO = OrderFrom2OderDTOConverter.convert(orderFrom);
        /** 转换之后以防购物车是空的判断一下. */
        if (CollectionUtils.isEmpty(oderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OderDTO createResult = orderService.create(oderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);

    }
    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OderDTO>> list(@RequestParam("openid") String openid,
                                        @RequestParam(value = "page",defaultValue = "0") Integer page,
                                        @RequestParam(value = "size",defaultValue = "10") Integer size){
        if (StringUtils.isEmpty(openid)){
            log.error("【查询订单】openid为空",openid);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest request = new PageRequest(page,size);
        Page<OderDTO> oderDTOPage = orderService.findList(openid,request);

        return ResultVOUtil.success(oderDTOPage.getContent());
    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId) {
        //TODO
        OderDTO orderDTO = buyerService.findOrderOne(openid ,orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单

    @PostMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam("orderId") String orderId) {
        OderDTO oderDTO = buyerService.cancelOrder(openid , orderId);
        return ResultVOUtil.success();
    }


}
