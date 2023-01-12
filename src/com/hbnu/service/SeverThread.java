package com.hbnu.service;

import com.hbnu.dao.ChatDao;
import com.hbnu.model.MsgBean;
import com.hbnu.model.User;
import com.hbnu.util.MyObjectInputStream;
import com.hbnu.util.MyObjectOutputStream;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SeverThread implements Runnable{
    private Socket socket;
    private static Map<String,Socket> onlineUsers=new ConcurrentHashMap<String, Socket>();;
    public SeverThread(Socket socket) {
        this.socket = socket;
    }
    public Socket getOnlineUserByUsername(String username){
        return onlineUsers.get(username);
    }
    public boolean isUserExist(String username){
        return onlineUsers.containsKey(username);
    }
    public void addOnlineUsers(String username,Socket socket){
        onlineUsers.put(username,socket);
    }
    public Map<String,Socket> getOnlineList(){
        return onlineUsers;
    }
    public void delOnlineUsers(String username){
        onlineUsers.remove(username);
    }

    @Override
    public void run() {
        while (true){
            //首页功能
            try {
                homeFunction(socket);
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
    //首页功能
    private void homeFunction(Socket socket) throws IOException {
        //获取流对象
        MyObjectOutputStream objectOutputStream = new MyObjectOutputStream(socket.getOutputStream());
        MyObjectInputStream objectInputStream = new MyObjectInputStream(socket.getInputStream());
        String choice=null;//接收客户端返回的选项
        while (true){
            try {
                choice = ((String) objectInputStream.readObject());//接收客户端返回的选项
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (choice!=null){
                switch (choice){
                    case "1":
                        try {
                            //服务端验证用户注册是否成功
                            User user = (User) objectInputStream.readObject();//接收客户端发送过来的用户
                            //将用户注册信息添加到数据库中
                            if (LoginService.register(user)){
                                System.out.println("用户「"+user.getUsername()+"」已注册");
                                objectOutputStream.writeObject("注册成功");
                            }else {
                                objectOutputStream.writeObject("用户名已存在");
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        objectOutputStream.reset();
                        break;
                    case "2":
                        //服务端验证用户登录是否成功
                        try {
                            User user = (User) objectInputStream.readObject();//接收客户端发送过来的用户
                            //判断用户是不是已经在线
                            if (!isUserExist(user.getUsername())) {
                                //验证登录
                                if (LoginService.login(user)) {
                                    System.out.println("用户「" + user.getUsername() + "」已上线");
                                    objectOutputStream.writeObject("登录成功");
                                    Thread.currentThread().setName(user.getUsername());
                                    addOnlineUsers(user.getUsername(), this.socket);//添加用户到在线用户列表
                                    chatFunction(socket, user);
                                } else {
                                    objectOutputStream.writeObject("用户名或密码错误");
                                }
                            }else {
                                objectOutputStream.writeObject("用户已在线");
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        objectOutputStream.reset();
                        break;
                    case "3":
                        //找回密码功能实现
                        try {
                            User user = (User) objectInputStream.readObject();//接收客户端发送过来的用户
                            String password;
                            if ((password=LoginService.recoverPassword(user))!=null){
                                objectOutputStream.writeObject(password);
                            }else {
                                objectOutputStream.writeObject("用户名或邮箱错误");
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        objectOutputStream.reset();
                        break;
                }
            }
        }
    }
    private void chatFunction(Socket socket,User user) throws IOException {
        //获取流对象
        MyObjectOutputStream objectOutputStream = new MyObjectOutputStream(socket.getOutputStream());
        MyObjectInputStream objectInputStream = new MyObjectInputStream(socket.getInputStream());
        String choice = null;//接收客户端返回的选项
        while (true) {
            try {
                choice = ((String) objectInputStream.readObject());//接收客户端返回的选项
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (choice != null) {
                //发送给客户端，让客户端知道该用什么接收
                if (!"2".equals(choice)&&!"3".equals(choice)){
                    objectOutputStream.writeObject(choice);
                }
                MsgBean msgBean = null;
                switch (choice) {
                    case "1":
                        //查看在线人员名单
                        List<String> nameList = new ArrayList<>();
                        for (Map.Entry<String, Socket> entry : getOnlineList().entrySet()) {
                            nameList.add(entry.getKey());
                        }
                        objectOutputStream.writeObject(nameList);
                        //关键
                        objectOutputStream.reset();
                        break;
                    case "2":
                        try {
                            msgBean = (MsgBean) objectInputStream.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        msgBean.setFrom(user);
                        //判断用户是否在线
                        if (isUserExist(msgBean.getTo())) {
                            //判断用户是不是自己
                            if (msgBean.getTo().equals(user.getUsername())) {
                                objectOutputStream.writeObject("不要给自己发消息");
                            } else {
                                privateChatting(msgBean);
                            }
                        }else {
                            //不存在的情况
                            objectOutputStream.writeObject("用户不存在或未上线");
                        }
                        objectOutputStream.reset();
                        break;
                    case "3":
                        //群聊
                        msgBean = null;
                        try {
                            msgBean = (MsgBean) objectInputStream.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        msgBean.setFrom(user);
                        groupChatting(user,msgBean);
                        break;
                    case "4":
                        //退出聊天
                        System.out.println("用户「"+user.getUsername()+"」已下线");
                        delOnlineUsers(user.getUsername());//退出登录后在在线用户列表中把用户删除
                        return;
                    case "5":
                        //账号注销
                        delOnlineUsers(user.getUsername());
                        ChatDao.delByUsername(user.getUsername());
                        System.out.println("用户「"+user.getUsername()+"」已注销");
                        objectOutputStream.writeObject("注销成功");
                        objectOutputStream.reset();
                        return;
                    case "6":
                        //修改密码
                        try {
                            //获取旧密码
                            String oldPassword = (String) objectInputStream.readObject();
                            String newPassword = (String) objectInputStream.readObject();
                            if (ChatService.checkOldPassword(user,oldPassword)){
                                ChatService.updatePassword(user,newPassword);
                                user.setPassword(newPassword);
                                objectOutputStream.writeObject("修改密码成功");
                            }else {
                                objectOutputStream.writeObject("旧密码错误");
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        objectOutputStream.reset();
                        break;
                }
            }
            objectOutputStream.reset();
        }
    }
    //私聊
    private void privateChatting(MsgBean msgBean) throws IOException {
        //目标线程
        OutputStream outputStream = getOnlineUserByUsername(msgBean.getTo()).getOutputStream();
        MyObjectOutputStream target = new MyObjectOutputStream(outputStream);
        target.writeObject("2");
        //把消息发送给目标线程
        target.reset();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String message="[私聊]"+simpleDateFormat.format(msgBean.getDate())+'\n'+msgBean.getFrom().getUsername()+":"+
                msgBean.getMsg();
        target.writeObject(message);
        target.reset();
    }
    //群聊
    private void groupChatting(User user,MsgBean msgBean) throws IOException {
        Map<String, Socket> onlineList = getOnlineList();
        for (Map.Entry<String, Socket> target : onlineList.entrySet()) {
            if (!target.getKey().equals(user.getUsername())) {
                MyObjectOutputStream targetStream = new MyObjectOutputStream(target.getValue().getOutputStream());
                targetStream.writeObject("3");
                targetStream.reset();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String message = "[群聊消息]" + simpleDateFormat.format(msgBean.getDate()) + '\n' + msgBean.getFrom().getUsername() + ":" +
                        msgBean.getMsg();
                targetStream.writeObject(message);
                targetStream.reset();
            }
        }
    }

}
