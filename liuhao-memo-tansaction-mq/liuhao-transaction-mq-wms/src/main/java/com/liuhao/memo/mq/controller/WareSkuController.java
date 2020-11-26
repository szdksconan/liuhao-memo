package com.liuhao.memo.mq.controller;

import java.util.Arrays;
import java.util.List;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuhao.memo.mq.bean.Resp;
import com.liuhao.memo.mq.po.WareSkuEntity;
import com.liuhao.memo.mq.service.WareSkuService;
import com.liuhao.memo.mq.vo.SkuLockVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;





/**
 * 商品库存
 */
@Api(tags = "商品库存 管理")
@RestController
@RequestMapping("wms/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping
    public Resp<Object> checkAndLockStore(@RequestBody List<SkuLockVO> skuLockVOS){

        String msg = this.wareSkuService.checkAndLockStore(skuLockVOS);
        if (StringUtils.isEmpty(msg)) {
            return Resp.ok(null);
        }
        return Resp.fail(msg);
    }

    @GetMapping("{skuId}")
    public Resp<List<WareSkuEntity>> queryWareSkusBySkuId(@PathVariable("skuId")Long skuId){

        List<WareSkuEntity> wareSkuEntities = wareSkuService.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId));
        return Resp.ok(wareSkuEntities);
    }







}
