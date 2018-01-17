package com.liuhao.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class PoolDayTimeServerLearn {

    final static int  PORT = 1314;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while (true){
               Socket socket = serverSocket.accept();
               executorService.submit(new dayTimeTask(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static class dayTimeTask implements Callable<Void> {


        private Socket connection;
        private dayTimeTask(Socket connection){
            this.connection = connection;
        }
        @Override
        public Void call() throws Exception {
            try (OutputStream outputStream = connection.getOutputStream();
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)){
                String now = new Date().toString();
                outputStreamWriter.write(now+"\r\n");
                outputStreamWriter.flush();
            }finally {
                     connection.close();
            }




            return null;
        }
    }




}
