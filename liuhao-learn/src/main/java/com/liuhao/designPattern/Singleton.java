package com.liuhao.designPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 单例模式
 */
public class Singleton {

    public static volatile Singleton instance = null;
    public List<String> list = null;
    private Singleton(){
        list = new ArrayList<>();
    }

    public static Singleton getInstance(){
        if(null == instance){
            synchronized (Singleton.class) {
                if(null == instance) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }



}
