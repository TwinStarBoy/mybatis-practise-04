package com.gupaoedu.mybatis.v2.plugin;

import java.lang.reflect.InvocationTargetException;

/**
 * 拦截器接口，所有自定义拦截器必须实现此接口
 */
public interface Interceptor {

    /**
     * 插件的核心逻辑实现
     * @param invovation
     * @return
     */
    Object interceptor(Invovation invovation) throws InvocationTargetException, IllegalAccessException, Throwable;

    /**
     * 对被拦截对象进行代理
     * @param target
     * @return
     */
    Object plugin(Object target);
}
