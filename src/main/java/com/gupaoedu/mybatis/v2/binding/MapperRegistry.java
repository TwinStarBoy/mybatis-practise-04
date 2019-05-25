package com.gupaoedu.mybatis.v2.binding;

import com.gupaoedu.mybatis.v2.session.DefaultSqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于维护接口和工厂类的关系，用于获取MapperProxy对象
 * 工厂类指定了POJO类型，用于处理结果集返回
 */
public class MapperRegistry {
    private static Map<Class<?> , MapperProxyFactory> kownMappers = new HashMap<Class<?> , MapperProxyFactory>();

    public <T> void addMapper(Class<T> clazz , Class pojo){
        kownMappers.put(clazz , new MapperProxyFactory(clazz,pojo));
    }

    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession){
        MapperProxyFactory mapperProxyFactory = kownMappers.get(clazz);
        if (mapperProxyFactory == null){
            throw new RuntimeException("Type " + clazz + " can not find");
        }

        return (T)mapperProxyFactory.newInstance(sqlSession);
    }
}
