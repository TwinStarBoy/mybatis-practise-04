package com.gupaoedu.mybatis.v2.executor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetHandler {

    public <T> T handle(ResultSet resultSet , Class type){

        Object pojo = null;
        try {
            pojo = type.newInstance();
            //遍历结果集
            if (resultSet.next()){
                for (Field feild : pojo.getClass().getDeclaredFields()){
                    setValue(pojo,feild,resultSet);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (T)pojo;
    }


    public void setValue(Object pojo , Field field , ResultSet resultSet){
        try {
            Method setMethod = pojo.getClass().getMethod("set" + firstWordCapital(field.getName()),field.getType());
            setMethod.invoke(pojo , getResult(resultSet,field));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Object getResult(ResultSet rs , Field field) throws SQLException {
        Class type = field.getType();

        String dataName = HumpToUnderline(field.getName());


        if (Integer.class == type) {
            return rs.getInt(dataName);
        } else if (String.class == type) {
            return rs.getString(dataName);
        } else if (Long.class == type) {
            return rs.getLong(dataName);
        } else if (Boolean.class == type) {
            return rs.getBoolean(dataName);
        } else if (Double.class == type) {
            return rs.getDouble(dataName);
        } else {
            return rs.getString(dataName);
        }

    }

    // 数据库下划线转Java驼峰命名
    public static String HumpToUnderline(String para){
        StringBuilder sb=new StringBuilder(para);
        int temp=0;
        if (!para.contains("_")) {
            for(int i=0;i<para.length();i++){
                if(Character.isUpperCase(para.charAt(i))){
                    sb.insert(i+temp, "_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 单词首字母大写
     */
    private String firstWordCapital(String word){
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }
}
