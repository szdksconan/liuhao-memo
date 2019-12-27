package com.liuhao.designPattern;

/**
 * 原型模式 clone对象 直接操作二进制流 不通过new 更效率
 *
 */
public class Prototype implements Cloneable{

    //重写clone 被复制对象必须继承cloneable 这个接口
    public Prototype clone(){
        Prototype prototype = null;
        try {
            prototype = (Prototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return prototype;
    }

}
class ConProtoType extends Prototype{

    public void show(){
        System.out.println("clone is happend "+ this.hashCode());
    }

}

class Test{
    public static void main(String[] args) {
        ConProtoType cp = new ConProtoType();
        for(int i=0;i<10;i++){
            ConProtoType cpnew = (ConProtoType) cp.clone();
            cpnew.show();
        }

    }
}