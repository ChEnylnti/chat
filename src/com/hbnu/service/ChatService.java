package com.hbnu.service;

import com.hbnu.dao.ChatDao;
import com.hbnu.dao.LoginDao;
import com.hbnu.model.User;

public class ChatService {
    public static boolean checkOldPassword(User user,String oldPassword){
        boolean result=false;
        if (oldPassword.equals(user.getPassword())){
            result=true;
        }
        return result;
    }
    public static void updatePassword(User user,String password){
        ChatDao.UpdatePassword(user,password);
    }
}
