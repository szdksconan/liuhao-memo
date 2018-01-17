package com.liuhao.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * 模拟RFC 868时间协议的 迭代协议服务器
 */
public class DayTimeServerLearn {

    public static final int PORT = 1137;

    public static void main(String[] args) {
        // 时间协议将时间起点设置为1900
        // 而DATE 类以1970年开始计算 利用这个参数 可以互相转换
        long differencebetweenepoch = 2208988800l;

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
           while (true){
               try(Socket socket = serverSocket.accept()){
                   OutputStream outputStream = socket.getOutputStream();
                   Date date = new Date();
                   long msSince1970 = date.getTime();
                   long secSince1970 = msSince1970/1000;
                   long secSince1900 = secSince1970 + differencebetweenepoch;

                   byte[] time = new byte[4];
                   time[0] = (byte)((secSince1900 & 0x00000000FF0000000L)>> 24);
                   time[1] = (byte)((secSince1900 & 0x0000000000FF0000L) >> 16);
                   time[2] = (byte)((secSince1900) & 0x000000000000FF00L >> 8);
                   time[3] = (byte)((secSince1900) & 0x00000000000000FFL >> 8);

                   outputStream.write(time);
                   outputStream.flush();
                   outputStream.close();
               }catch (IOException e){
                   e.printStackTrace();
               }
           }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
