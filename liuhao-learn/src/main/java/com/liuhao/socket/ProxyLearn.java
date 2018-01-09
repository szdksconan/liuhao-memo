package com.liuhao.socket;

import javafx.scene.media.SubtitleTrack;
import jdk.nashorn.internal.ir.WhileNode;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * 局部代理 针对于某个连接
 */
public class ProxyLearn {

    static final String PROXY_HOST = "218.14.121.230";
    static final int PROXY_PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        //构建一个代理地址
        SocketAddress proxyAddress = new InetSocketAddress(PROXY_HOST, PROXY_PORT);
        //构建代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);
        //通过代理对象构建连接对象
        try (Socket socket = new Socket();) {
            String host = "www.oschina.net";
            int port = 80;
            //构建一个 远程地址 对象
            SocketAddress remote = new InetSocketAddress(host, port);
            socket.connect(remote, 3000);
            socket.setSoTimeout(3000);

            try (BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), "utf-8");) {
                osw.write("GET " + "/" + " HTTP/1.1\r\n");
                osw.write("Host: " + host + " \r\n");
                //http协议必须在报文头后面再加一个换行，通知服务器发送完成，不然服务器会一直等待
                osw.write("\r\n");
                osw.flush();
                //socket.shutdownOutput();
                System.out.println("*******开始响应********");

                String line;
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
