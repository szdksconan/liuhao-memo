package com.liuhao.memo.mq.vo;

import com.liuhao.memo.mq.po.AddressEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubmitVO {

    private String orderToken; // f防重
    private AddressEntity address;
    private Integer payType;
    private String deliveryCompany;
    private List<OrderItemVO> items;
    private Integer bounds;
    private BigDecimal totalPrice; // 校验价格
    private Long userId;
}
