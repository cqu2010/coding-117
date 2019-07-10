package com.imooc.service;

import com.imooc.dto.OderDTO;
import com.lly835.bestpay.model.PayResponse;

/**
 * 支付
 * Created By 李世豪
 * 2019-6-26
 */
public interface PayService {

    PayResponse create(OderDTO oderDTO);
}
