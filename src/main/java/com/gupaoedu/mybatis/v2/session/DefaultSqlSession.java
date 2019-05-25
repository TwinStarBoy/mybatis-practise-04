package com.gupaoedu.mybatis.v2.session;

import com.gupaoedu.mybatis.v2.executor.Executor;

/**
 * MeMbatisAPI,提供给应用层使用
 */
public class DefaultSqlSession {

    public Executor executor;
    public Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = configuration.newExecutor();
    }

    public Configuration getConfiguration() {
        return configuration;
    }


    //获取代理类
    public <T> T getMapper(Class<?> clazz){
        return (T) configuration.getMapper(clazz,this);
    }

    public <T> T selectOne(String statementId , Object[] params , Class pojo){
        String statement = getConfiguration().getMapperStatemetn(statementId);

        return executor.query(statement,params,pojo);
    }
}
