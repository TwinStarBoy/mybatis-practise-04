package com.gupaoedu.mybatis.v2.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数处理器
 */
public class ParameterHandler {

    private PreparedStatement psmt ;

    public ParameterHandler(PreparedStatement psmt) {
        this.psmt = psmt;
    }

    public void setParameter (Object[] parameters){

        //
        try {
            for (int i=0;i<parameters.length;i++){
                int k = i+1;
                Object parameter = parameters[i];
                if (parameter instanceof Integer){
                    psmt.setInt(k, (Integer) parameter);
                }else if (parameter instanceof Long){
                    psmt.setLong(k, (Long) parameter);
                }else if (parameter instanceof String){
                    psmt.setString(k, (String) parameter);
                }else if (parameter instanceof Boolean){
                    psmt.setBoolean(k, (Boolean) parameter);
                }else {
                    psmt.setString(k,String.valueOf(parameter));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
