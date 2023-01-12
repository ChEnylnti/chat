package com.hbnu.dao;

import com.hbnu.model.User;
import com.hbnu.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatDao {
    public static void delByUsername(String username){
        Connection connection= null;
        PreparedStatement preparedStatement=null;
        String sql="delete from tb_user where username =?";
        try {
            connection= DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.release(preparedStatement,connection);
        }
    }
    public static void UpdatePassword(User user,String password){
        Connection connection= null;
        PreparedStatement preparedStatement=null;
        String sql="update tb_user set password=? where username=?";
        try {
            connection= DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,password);
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.release(preparedStatement,connection);
        }
    }
}
