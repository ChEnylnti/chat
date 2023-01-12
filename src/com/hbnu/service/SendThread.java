package com.hbnu.service;

import com.hbnu.model.MsgBean;
import com.hbnu.util.MyObjectOutputStream;
import com.hbnu.view.Page;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class SendThread implements Runnable{
    private Socket socket;

    public SendThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
            try {
                send(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    private void send(Socket socket) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //获取流对象
        MyObjectOutputStream objectOutputStream = null;
        String choice;//接收用户输入的选项
        while (true){
            Page.getChatMenu();
            choice=scanner.next();//获取用户输入的选项
            objectOutputStream = new MyObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(choice);//返回选项给服务端
            MsgBean msgBean =null;
            switch (choice){//根据选择进行操作
                case "1":
                    break;
                case "2":
                    //私聊
                    msgBean = Page.getMsgBean();
                    objectOutputStream.writeObject(msgBean);//把消息对象发送给服务端
                    objectOutputStream.reset();
                    break;
                case "3":
                    //群聊
                    msgBean = Page.getGMsgBean();
                    objectOutputStream.writeObject(msgBean);
                    objectOutputStream.reset();
                    break;
                case "4":
                case "5":
                    //退出聊天
                    //账号注销
                    //关闭发送线程
                    Thread.currentThread().interrupt();
                    return;
                case "6":
                    //修改密码
                    //旧密码
                    String oldPassword = Page.getPassword("旧");
                    //将数据发给服务器校验
                    objectOutputStream.writeObject(oldPassword);
                    objectOutputStream.reset();
                    String newPassword = Page.getPassword("新");
                    //将新密码发给服务器修改
                    objectOutputStream.writeObject(newPassword);
                    break;
            }
        }
    }
}
