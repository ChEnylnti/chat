package com.hbnu.view;

import com.hbnu.model.MsgBean;
import com.hbnu.model.User;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

//系统首页
public class Page {
    //初始菜单
    public static void getInitMenu(){//获取初始菜单
        System.out.println("请选择功能（输入数字）：");
        System.out.println("1.注册\t 2.登录\t 3.找回密码\t 4.退出");
    }
    //注册用户
    public static User register(){
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        while ("".equals(username)){
            System.out.println("用户名不能为空，请重新输入");
            username=scanner.nextLine();
        }
        String password;
        for (int i = 0; ; i++) {
            if (i>3){
                System.out.println("失败次数过多");
                System.exit(1);//退出系统
            }
            if (i>=1){
                System.out.println("密码格式错误，请重新输入:");
                password=scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+$",password)){//检查密码格式
                    break;
                }
            }else if (i==0) {
                System.out.println("请输入密码:");
                password = scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+$",password)){//检查密码格式
                    break;
                }
            }
        }
        String email;
        for (int i = 0; ; i++) {
            if (i>3){
                System.out.println("失败次数过多");
                System.exit(1);//退出系统
            }
            if (i>=1){
                System.out.println("邮箱错误，请重新输入:");
                email=scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+",email)){//检查邮箱格式
                    break;
                }
            }else if (i==0) {
                System.out.println("请输入邮箱:");
                email = scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+",email)){//检查邮箱格式
                    break;
                }
            }
        }
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        return user;
    }
    //用户登录
    public static User login(){
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        while ("".equals(username)){
            System.out.println("用户名不能为空，请重新输入：");
            username=scanner.nextLine();
        }
        System.out.println("请输入密码：");
        String password=scanner.nextLine();
        while ("".equals(password)){
            System.out.println("密码不能为空，请重新输入：");
            password=scanner.nextLine();
        }
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
    //找回密码
    public static User recoverPassword(){
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入用户名：");
        String username = scanner.nextLine();
        while ("".equals(username)){
            System.out.println("用户名不能为空，请重新输入");
            username=scanner.nextLine();
        }
        String email;
        for (int i = 0; ; i++) {
            if (i>3){
                System.out.println("失败次数过多");
                System.exit(1);//退出系统
            }
            if (i>=1){
                System.out.println("邮箱错误，请重新输入:");
                email=scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+@+$",email)){//检查密码格式
                    break;
                }
            }else if (i==0) {
                System.out.println("请输入邮箱:");
                email = scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+",email)){//检查密码格式
                    break;
                }
            }
        }
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }
    //获取聊天菜单
    public static void getChatMenu(){
        System.out.println("请选择功能（输入数字）：");
        System.out.println("1.查看在线人员名单\t 2.私聊\t 3.群聊\t 4.退出聊天\t 5.账号注销\t 6.修改密码");
    }
    //修改密码时需要先获取旧密码进行校验
    public static String getPassword(String msg){
        Scanner scanner = new Scanner(System.in);
        String password;
        for (int i = 0; ; i++) {
            if (i>3){
                System.out.println("失败次数过多");
                System.exit(1);//退出系统
            }
            if (i>=1){
                System.out.println("密码格式错误，请重新输入:");
                password=scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+$",password)){//检查密码格式
                    break;
                }
            }else if (i==0) {
                System.out.println("请输入"+msg+"密码:");
                password = scanner.nextLine();
                if (Pattern.matches("^[A-Za-z0-9]+$",password)){//检查密码格式
                    break;
                }
            }
        }
        return password;
    }
    //定义私聊消息
    public static MsgBean getMsgBean(){
        Scanner scanner = new Scanner(System.in);
        MsgBean msgBean = new MsgBean();
        System.out.println("To:");
        String to = scanner.nextLine();
        msgBean.setTo(to);
        System.out.println("输入要发送的消息");
        String msg = scanner.nextLine();
        msgBean.setMsg(msg);
        //设置日期
        msgBean.setDate(new Date());
        return msgBean;
    }
    //定义群聊消息
    public static MsgBean getGMsgBean(){
        Scanner scanner = new Scanner(System.in);
        MsgBean msgBean = new MsgBean();
        System.out.println("输入要发送的消息");
        String msg = scanner.nextLine();
        msgBean.setMsg(msg);
        msgBean.setDate(new Date());
        return msgBean;
    }
}
