package com.gupaoedu.mybatis.v2.binding;

import com.gupaoedu.mybatis.v2.session.DefaultSqlSession;

import java.lang.reflect.Proxy;

/**
 * 用于产生mapperProxy代理类
 * @param <T>
 */
public class MapperProxyFactory<T> {
    private Class<T> mapperInteface;
    private Class object;

    public MapperProxyFactory(Class<T> mapperInteface, Class object) {
        this.mapperInteface = mapperInteface;
        this.object = object;
    }

    public T newInstance(DefaultSqlSession sqlSession){
        return (T) Proxy.newProxyInstance(mapperInteface.getClassLoader(),new Class[] {mapperInteface},new MapperProxy(sqlSession,object));
    }
}
