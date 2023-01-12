package com.hbnu.dao;

import com.hbnu.model.User;
import com.hbnu.util.DBUtil;

import java.sql.*;

//数据库接口，用于添加修改删除
public class LoginDao {
    public static User getUserByUsername(String username){//根据用户名查用户
        Connection connection= null;
        User user = null;
        Statement statement=null;
        ResultSet resultSet=null;
        String sql = "select * from tb_user where username='" + username + "'";
        try {
            connection=DBUtil.getConnection();
            statement = connection.createStatement();
            resultSet=statement.executeQuery(sql);
            if (resultSet.next()){
                user=new User();
                user.setUsername(username);
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.release(resultSet,statement,connection);
        }
        return user;
    }
    public static int addUser(User user){//添加用户
        Connection connection= null;
        PreparedStatement preparedStatement=null;
        String sql="insert into tb_user (username,password,email) values(?,?,?);";
        int result=0;
        try {
            connection=DBUtil.getConnection();
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setString(3,user.getEmail());
            result = preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            DBUtil.release(preparedStatement,connection);
        }
        return result;
    }

}
