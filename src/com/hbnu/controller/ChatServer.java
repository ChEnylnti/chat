package com.hbnu.controller;

import com.hbnu.service.SeverThread;
import com.hbnu.util.BannerUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//服务启动类
public class ChatServer {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9999);//监听9999端口
            BannerUtil.getBanner("src//p.txt");
            System.out.println("聊天室服务端已启动，等待客户端用户连接。。。");
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new SeverThread(socket)).start();
            }
        } catch (IOException e) {
        }

    }

}
