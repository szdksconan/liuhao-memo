package com.liuhao.lemo.liuhaospringbootnacosconsumer.rpc;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "services-provider")
@RefreshScope
public interface ProviderServer{

    @GetMapping("hello")
    public String hello();

}
