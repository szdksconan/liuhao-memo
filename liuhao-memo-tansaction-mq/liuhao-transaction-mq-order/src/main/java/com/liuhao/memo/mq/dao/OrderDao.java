package com.liuhao.memo.mq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuhao.memo.mq.po.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    public int closeOrder(String orderToken);

    public int payOrder(String orderToken);
	
}
