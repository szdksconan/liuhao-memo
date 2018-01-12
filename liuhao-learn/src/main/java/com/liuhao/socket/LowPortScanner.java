package com.liuhao.socket;

import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 查询端口是否有服务
 */
public class LowPortScanner {
    
    static final String host = "localhost";

    public static void main(String[] args) {
        for (int i = 0; i < 1024; i++) {
            try(Socket s= new Socket(host,i)){
                System.out.println("this is a server  "+host+":"+i);
            }catch (UnknownHostException ue){
                ue.printStackTrace();
                break;
            }catch (Exception e){
                System.out.println("is not a server");
            }
        }
    }
}
