package com.liuhao.memo.mq.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuhao.memo.mq.po.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
