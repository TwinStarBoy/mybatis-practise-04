package com.gupaoedu.mybatis.v2.executor;

import com.gupaoedu.mybatis.v2.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的执行器，用于装饰基本执行器
 */
public class CachingExecutor implements Executor {

    private Executor delegate;
    private static final Map<Integer , Object> cache = new HashMap<Integer , Object>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(String statement, Object[] paramter, Class pojo) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(joinStr(paramter));

        //是否拿到缓存
        if (cache.containsKey(cacheKey.getCode())){
            //命中缓存
            System.out.println("[命中缓存]");
            return (T) cache.get(cacheKey.getCode());
        }else {
            Object object = delegate.query(statement , paramter , pojo);
            cache.put(cacheKey.getCode() , object);
            return (T) object;
        }

    }

    //为了命中缓存，把Object[] 转换成逗号拼接的字符串，因为对象的hashCode不一样
    public String joinStr(Object[] objects){
        StringBuffer sb = new StringBuffer();
        if (objects != null && objects.length > 0){
            for (Object object : objects){
                sb.append(object.toString() + ",");
            }
        }
        int length = sb.length();
        if (length > 0 ){
            sb.deleteCharAt(length-1);
        }
        return sb.toString();
    }
}
