package com.liuhao.designPattern.flyweight;

public interface FlyWeight {
    //对外状态对象
    void operation(String name);
    //
    String getType();
}
