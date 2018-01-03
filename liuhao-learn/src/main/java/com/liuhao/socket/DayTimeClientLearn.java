package com.liuhao.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 时间服务器测试
 */
public class DayTimeClientLearn {

    final static String host = "time.nist.gov";

    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket(host,13)) {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"ASCII");
            StringBuilder sb  = new StringBuilder();
            int c = 0;
            while ((c = inputStream.read())!=-1){
                sb.append((char)c);
            }
            System.out.println(sb.toString());
        }
    }


}
