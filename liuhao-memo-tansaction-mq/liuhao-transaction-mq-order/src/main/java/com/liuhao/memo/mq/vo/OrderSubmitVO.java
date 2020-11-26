package com.liuhao.memo.mq.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubmitVO {

    private String orderToken; // f防重
    private String skuCode;
    private Integer count;
    private BigDecimal totalPrice; // 校验价格
    private String userId;

}
