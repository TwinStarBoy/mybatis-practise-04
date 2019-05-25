package com.gupaoedu.mybatis;

import com.gupaoedu.mybatis.v2.parameter.ParameterHandler;
import com.gupaoedu.mybatis.v2.session.Configuration;

import java.sql.*;

public class TestPreParedStatement {


    public static void main(String[] args) {
        System.out.println("start");
        String sql = "select * from blog where bid = ? and name = ?";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Object result = null;
        conn = getConnection();
        try {
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,1);
            preparedStatement.setString(2,"张三 ; drop table blog;");

            rs = preparedStatement.executeQuery();
            while (rs.next()){
                System.out.println(rs.getInt(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Connection getConnection(){
        String driver = Configuration.properties.getString("jdbc.driver");
        String url = Configuration.properties.getString("jdbc.url");
        String username = Configuration.properties.getString("jdbc.username");
        String password = Configuration.properties.getString("jdbc.password");

        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
