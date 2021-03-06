package com.liuhao.memo.liuhaospringbootnacosprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    @Value("${name}")
    private String name;

    @GetMapping("providerHello")
    public String providerHello(){
        return name+ " provider hello";
    }



}
