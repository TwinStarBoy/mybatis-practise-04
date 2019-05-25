package com.gupaoedu.mybatis.v2.executor;

import com.gupaoedu.mybatis.v2.parameter.ParameterHandler;
import com.gupaoedu.mybatis.v2.session.Configuration;

import java.sql.*;

/**
 * 封装JDBC操作
 */
public class StatementHandler {
    private ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <T> T query(String statement, Object[] paramter, Class pojo) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        Object result = null;
        conn = getConnection();

        try {
            preparedStatement = conn.prepareStatement(statement);
            ParameterHandler parameterHandler = new ParameterHandler(preparedStatement);
            parameterHandler.setParameter(paramter);
            preparedStatement.execute();

            result = resultSetHandler.handle(preparedStatement.getResultSet(),pojo);

            return (T)result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
        }
        //
        return null;
    }

    private Connection getConnection(){
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
