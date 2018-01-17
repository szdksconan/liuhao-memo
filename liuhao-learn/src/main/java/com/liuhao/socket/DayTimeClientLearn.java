package com.liuhao.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * 时间服务器测试
 */
public class DayTimeClientLearn {

    final static String HOST = "time.nist.gov";
    final static int PORT = 13;
    final static String LOCALHOST = "localhost";
    final static int LOCALHOSTPORT = 1137;

    public static void main(String[] args) throws IOException {
        try(Socket socket = new Socket(LOCALHOST,LOCALHOSTPORT)) {
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
