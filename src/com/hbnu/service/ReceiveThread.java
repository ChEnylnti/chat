package com.hbnu.service;


import com.hbnu.util.MyObjectInputStream;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReceiveThread implements Runnable {
    private Socket socket;

    public ReceiveThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
            try {
                receive(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void receive(Socket socket) throws IOException {

        MyObjectInputStream objectInputStream = new MyObjectInputStream(socket.getInputStream());
        String choice=null;//判断服务端发回的结果集
        while (true) {
            try {
                choice = ((String) objectInputStream.readObject());
                //只输出中文
                String regex = "[\u4e00-\u9fa5。，？”“《》：！——-、]";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(choice);
                if (matcher.find()){
                    System.out.println(choice);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (choice != null) {
                switch (choice) {//根据选择读取结果集
                    case "1":
                        try {
                            //查看在线人员名单
                            List<String> list = (List<String>) objectInputStream.readObject();
                            System.out.println("在线人数为："+list.size());
                            System.out.println("在线列表："+list);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "2":
                    case "3":
                        //读取聊天
                        try {
                            String msg = (String) objectInputStream.readObject();
                            System.out.println(msg);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "4":
                        //关闭接收线程
                        Thread.currentThread().interrupt();
                        //退出聊天
                        return;
                    case "5":
                        try {
                            //账号注销
                            String msg1 = (String) objectInputStream.readObject();
                            System.out.println(msg1);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        //注销完自动退出账号
                        Thread.currentThread().interrupt();
                        return;
                    case "6":
                        //修改密码
                        //旧密码
                        String msg2 = null;
                        try {
                            msg2 = (String) objectInputStream.readObject();
                            System.out.println(msg2);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }
    }
}