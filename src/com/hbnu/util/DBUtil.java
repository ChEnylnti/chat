package com.hbnu.util;

import java.sql.*;
import java.util.ResourceBundle;

public class DBUtil {
    private static Connection connection;
    private static String driverClass;
    private static String url;
    private static String user;
    private static String password;
    public static Connection getConnection() throws SQLException {
        ResourceBundle application = ResourceBundle.getBundle("application");
        String driverClass = application.getString("driverClass");
        String url = application.getString("url");
        String user = application.getString("user");
        String password = application.getString("password");
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(url,user,password);
    }
    public static void release(Statement stmt,Connection conn){
        if (stmt!=null){
            try{
                stmt.close();
            }catch (SQLException e){
                System.out.println("stmt释放异常:"+e.getMessage());
            }
            stmt=null;
        }
        if (conn !=null){
            try{
                conn.close();
            }catch (SQLException e){
                System.out.println("conn释放异常："+e.getMessage());
            }
            conn=null;
        }
    }
    public static void release(PreparedStatement pstmt,Connection conn){
        if (pstmt!=null){
            try{
                pstmt.close();
            }catch (SQLException e){
                System.out.println("pstmt释放异常:"+e.getMessage());
            }
            pstmt=null;
        }
        if (conn !=null){
            try{
                conn.close();
            }catch (SQLException e){
                System.out.println("conn释放异常："+e.getMessage());
            }
            conn=null;
        }
    }
    public static void release(ResultSet rs,Statement stmt, Connection conn){
        if (rs!=null){
            try{
                rs.close();
            }catch (SQLException e){
                System.out.println("rs释放异常"+e.getMessage());
            }
            rs=null;
        }
        release(stmt, conn);
    }
}
