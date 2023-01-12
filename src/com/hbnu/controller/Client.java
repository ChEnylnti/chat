package com.hbnu.controller;

import com.hbnu.model.User;
import com.hbnu.service.ReceiveThread;
import com.hbnu.service.SendThread;
import com.hbnu.util.BannerUtil;
import com.hbnu.util.MyObjectInputStream;
import com.hbnu.util.MyObjectOutputStream;
import com.hbnu.view.Page;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//聊天客户端
public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            while (true){
                //首页功能
                BannerUtil.getBanner("src//banner.txt");
                System.out.println("欢迎来到聊天室");
                homeFunction(socket);
                BannerUtil.getBanner("src//target.txt");
                System.out.println("您已登录可以开始聊天啦");
                chatFunction(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //首页功能
    private static void homeFunction(Socket socket) throws IOException {
        Scanner scanner = new Scanner(System.in);
        //获取流对象
        MyObjectOutputStream objectOutputStream = new MyObjectOutputStream(socket.getOutputStream());
        MyObjectInputStream objectInputStream = new MyObjectInputStream(socket.getInputStream());

        String choice;//接收用户输入的选项
        while (true){
            Page.getInitMenu();
            choice=scanner.next();//获取用户输入的选项
            objectOutputStream.writeObject(choice);//返回选项给服务端
            switch (choice){//根据选择进行操作
                case "1":
                    //注册功能
                    //获取用户注册信息
                    User register = Page.register();
                    //将注册的用户信息发送给服务器
                    objectOutputStream.writeObject(register);
                    try {
                        //接收服务端返回的注册信息
                        String msg = (String) objectInputStream.readObject();
                        System.out.println(msg);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    //登录功能
                    //获取登录信息
                    User login = Page.login();
                    //将登录信息发给服务器
                    objectOutputStream.writeObject(login);
                    try {
                        //接收服务端返回的注册信息
                        String msg = (String) objectInputStream.readObject();
                        System.out.println(msg);
                        if ("登录成功".equals(msg)){//如果后台成功登录，前端也要进入聊天界面
                            return;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    //找回密码功能
                    //获取用户名和邮箱信息
                    User recover = Page.recoverPassword();
                    //将登录信息发给服务器
                    objectOutputStream.writeObject(recover);
                    try {
                        //接收服务端返回的信息
                        String msg = (String) objectInputStream.readObject();
                        System.out.println(msg);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    //退出功能
                    System.exit(1);
                    break;
            }
        }
    }
    //聊天功能 1.查看在线人员名单	 2.私聊	 3.群聊	 4.退出聊天	 5.账号注销	 6.修改密码
    private static void chatFunction(Socket socket) throws InterruptedException {

        //开启发送线程
        new Thread(new SendThread(socket)).start();

        //开启接收线程
        Thread thread = new Thread(new ReceiveThread(socket));
        thread.start();
        thread.join();

    }
}
