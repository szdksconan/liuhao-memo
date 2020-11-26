package com.liuhao.nio;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChanneTest {


    public void readFileTest() throws Exception {
        File file = new File("readFileTestTxt.txt");
        if(file.exists()) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                StringBuilder sb = new StringBuilder();
                while (fileChannel.read(byteBuffer) != -1) {
                    byteBuffer.flip();
                    String content = new String(byteBuffer.array(),"gbk");
                    System.out.println(content);
                }
            } finally {
                fileChannel.close();
            }
        }
    }

    public void writeFileTest() throws Exception{
        File file = new File("readFileTestTxt.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.seek(randomAccessFile.length());
        FileChannel channel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer =  ByteBuffer.wrap(" this is writeContent".getBytes());
        channel.write(byteBuffer);
        channel.close();
    }



    public static void main(String[] args) throws Exception {
        FileChanneTest fileChanneTest = new FileChanneTest();
        //fileChanneTest.readFileTest();
        fileChanneTest.writeFileTest();
    }



}
