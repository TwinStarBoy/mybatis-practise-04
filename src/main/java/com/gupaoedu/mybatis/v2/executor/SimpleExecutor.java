package com.gupaoedu.mybatis.v2.executor;

public class SimpleExecutor implements Executor {
    @Override
    public <T> T query(String statement, Object[] paramter, Class pojo) {
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.query(statement,paramter,pojo);
    }
}
