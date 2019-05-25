package com.gupaoedu.mybatis.v2.executor;

public interface Executor {
    <T> T query(String statement,Object[] paramter,Class pojo);
}
