package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.utils.keyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By 李世豪
 * 2019-5-24
 */
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductService productService;
    @Autowired
     private  OrderDetailRepository orderDetailRepository;
    @Autowired
     private  OrderMasterRepository orderMasterRepository;
    @Override
    public OderDTO create(OderDTO oderDTO) {
        /** 订单创建的时候orderId就生成了 .*/
        String orderId = keyUtil.genUniqueKey();
       /* 定义总价 . */
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        /** 库存 . */
       // List<CartDTO> cartDTOList =new ArrayList<>();
        //1. 查询商品（数量,价格）
       for (OrderDetail orderDetail :oderDTO.getOrderDetailList()){
        ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
        if (productInfo == null){
          throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
           //2.计算订单总价
           orderAmount=productInfo.getProductPrice()
                   .multiply( new BigDecimal(orderDetail.getProductQuantity()))
                   .add(orderAmount);
           //订单详情入库

           orderDetail.setDetailId(keyUtil.genUniqueKey());
           orderDetail.setOrderId(orderId);
           /** Spring 提供的属性拷贝 BeanUtils. */
           BeanUtils.copyProperties(productInfo , orderDetail);
           orderDetailRepository.save(orderDetail);
           //CartDTO cartDTO =new CartDTO(orderDetail.getProductId() ,orderDetail.getProductQuantity());
           //cartDTOList.add(cartDTO);
       }

        //3.写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        oderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(oderDTO ,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4扣库存
        List<CartDTO> cartDTOList = oderDTO.getOrderDetailList().stream().map(e ->
        new CartDTO(e.getProductId() ,e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);  //用productInfo的库存减去CartDTO的数量，然后就是库存的数量

        return oderDTO;
    }

    @Override
    public OderDTO findOne(String oderId) {
        OrderMaster orderMaster =orderMasterRepository.findOne(oderId);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(oderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw  new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OderDTO oderDTO = new OderDTO();
        BeanUtils.copyProperties(orderMaster,oderDTO);
        oderDTO.setOrderDetailList(orderDetailList);
        return oderDTO;

    }

    @Override
    public Page<OderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage =orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);

        List<OderDTO> oderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

       return new PageImpl<OderDTO>(oderDTOList ,pageable ,orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OderDTO cancel(OderDTO oderDTO) {
        OrderMaster orderMaster = new OrderMaster();

        //判断订单的状态
        /** 拿到的订单状态不等于新下单的状态 .*/
        if (!oderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", oderDTO.getOrderId(), oderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        oderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(oderDTO ,orderMaster);
       /** 返回更新之后的对象 . */
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        /** 如果是空的话进行判断一下. */
        if (updateResult == null){
            log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(oderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情,oderDTO={}",oderDTO);
            throw  new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList =oderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId() ,e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //如果已支付,需要退款
        if (oderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }
        return oderDTO;
    }

    @Override
    public OderDTO finish(OderDTO oderDTO) {
        //判断订单状态
        if (!oderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", oderDTO.getOrderId(), oderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        oderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(oderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return oderDTO;
    }

    @Override
    @Transactional
    public OderDTO paid(OderDTO oderDTO) {
        //判断订单状态
        if (!oderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", oderDTO.getOrderId(), oderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }


        //判断支付状态
        if (!oderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", oderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        oderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(oderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return oderDTO;

    }
}
