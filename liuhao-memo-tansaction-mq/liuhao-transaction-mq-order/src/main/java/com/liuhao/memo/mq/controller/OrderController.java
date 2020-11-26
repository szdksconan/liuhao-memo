package com.liuhao.memo.mq.controller;


import com.liuhao.memo.mq.bean.Resp;
import com.liuhao.memo.mq.po.OrderEntity;
import com.liuhao.memo.mq.service.OrderService;
import com.liuhao.memo.mq.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单
 */
@Api(tags = "订单 管理")
@RestController
@RequestMapping("oms/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Resp<OrderEntity> saveOrder(@RequestBody OrderSubmitVO submitVO){

        OrderEntity orderEntity = this.orderService.saveOrder(submitVO);
        return Resp.ok(orderEntity);
    }


}
