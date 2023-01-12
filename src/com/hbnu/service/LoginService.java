package com.hbnu.service;

import com.hbnu.dao.LoginDao;
import com.hbnu.model.User;

public class LoginService {

    public static boolean register(User user){
        String inputUsername = user.getUsername();
        User userByUsername = LoginDao.getUserByUsername(inputUsername);
        if (userByUsername!=null){//数据库中已有该用户
            return false;
        }else {
            LoginDao.addUser(user);
            return true;
        }
    }
    public static boolean login(User user){
        boolean result=false;
        String inputUsername = user.getUsername();
        User userByUsername = LoginDao.getUserByUsername(inputUsername);
        if (userByUsername!=null) {
            String password = userByUsername.getPassword();
            if (userByUsername != null) {
                if (user.getPassword().equals(password)) {
                    result = true;
                }
            }
        }
        return result;
    }
    public static String recoverPassword(User user){
        String inputUsername = user.getUsername();
        User userByUsername = LoginDao.getUserByUsername(inputUsername);
        String password = null;
        if (userByUsername!=null){
            if (userByUsername.getEmail().equals(user.getEmail())){
                password = userByUsername.getPassword();
            }
        }
        return password;
    }
}
