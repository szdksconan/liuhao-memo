package com.liuhao.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PoolDayTimeServerLearn {

    private final static Logger errorLogger = Logger.getLogger("error");
    private final static Logger auditLogger = Logger.getLogger("request");

    final static int  PORT = 1314;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while (true){
               Socket socket = serverSocket.accept();
               executorService.submit(new dayTimeTask(socket));
            }

        } catch (IOException e) {
            errorLogger.log(Level.SEVERE ,"Could not start server"+e);
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
            String now = new Date().toString();
            auditLogger.info(now +" "+connection.getInetAddress().getHostAddress());
            try (OutputStream outputStream = connection.getOutputStream();
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)){

                outputStreamWriter.write(now+"\r\n");
                outputStreamWriter.flush();
            }finally {
                     connection.close();
            }




            return null;
        }
    }




}
