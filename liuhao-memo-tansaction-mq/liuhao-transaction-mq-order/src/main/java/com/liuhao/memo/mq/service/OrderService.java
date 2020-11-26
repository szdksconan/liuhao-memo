package com.liuhao.memo.mq.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuhao.memo.mq.dao.OrderDao;
import com.liuhao.memo.mq.dao.OrderItemDao;
import com.liuhao.memo.mq.po.AddressEntity;
import com.liuhao.memo.mq.po.OrderEntity;
import com.liuhao.memo.mq.po.OrderItemEntity;
import com.liuhao.memo.mq.vo.OrderItemVO;
import com.liuhao.memo.mq.vo.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderDao, OrderEntity> {

    @Autowired
    OrderItemDao orderItemDao;

    @Transactional
    public OrderEntity saveOrder(OrderSubmitVO submitVO) {

        // 保存orderEntity
        AddressEntity address = submitVO.getAddress();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setReceiverRegion(address.getRegion());
        orderEntity.setReceiverProvince(address.getProvince());
        orderEntity.setReceiverPostCode(address.getPostCode());
        orderEntity.setReceiverPhone(address.getPhone());
        orderEntity.setReceiverName(address.getName());
        orderEntity.setReceiverDetailAddress(address.getDetailAddress());
        orderEntity.setReceiverCity(address.getCity());
        orderEntity.setMemberUsername("liuhao");
        orderEntity.setMemberId(submitVO.getUserId());

        orderEntity.setDeleteStatus(0);
        orderEntity.setStatus(0);

        orderEntity.setCreateTime(new Date());
        orderEntity.setModifyTime(orderEntity.getCreateTime());
        orderEntity.setDeliveryCompany(submitVO.getDeliveryCompany());
        orderEntity.setSourceType(1);
        orderEntity.setPayType(submitVO.getPayType());
        orderEntity.setTotalAmount(submitVO.getTotalPrice());
        orderEntity.setOrderSn(submitVO.getOrderToken());
        this.save(orderEntity);
        Long orderId = orderEntity.getId();

        // 保存订单详情OrderItemEntity
        List<OrderItemVO> items = submitVO.getItems();
        items.forEach(item -> {
            OrderItemEntity itemEntity = new OrderItemEntity();
            itemEntity.setSkuId(item.getSkuId());

            itemEntity.setSkuPrice(itemEntity.getSkuPrice());
            itemEntity.setSkuAttrsVals(itemEntity.getSkuAttrsVals());
            itemEntity.setCategoryId(itemEntity.getCategoryId());
            itemEntity.setOrderId(orderId);
            itemEntity.setOrderSn(submitVO.getOrderToken());
            itemEntity.setSpuId(itemEntity.getSpuId());
            itemEntity.setSkuName(itemEntity.getSkuName());
            itemEntity.setSkuQuantity(item.getCount());

            this.orderItemDao.insert(itemEntity);
        });

        // 订单创建之后，在响应之前发送延时消息，达到定时关单的效果
        //this.amqpTemplate.convertAndSend("GMALL-ORDER-EXCHANGE", "order.ttl", submitVO.getOrderToken());

        return orderEntity;
    }


}
