package com.liuhao.memo.mq.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVO {

    private Long skuId;
    private String title;
    private String defaultImage;
    private BigDecimal price; // 数据库价格
    private Integer count;
    private Boolean store;
    private BigDecimal weight;
}
