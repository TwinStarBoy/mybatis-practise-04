package com.gupaoedu.mybatis.v2.cache;

/**
 * 缓存key
 */
public class CacheKey {
    private static final int DEFAULT_HASHCODE = 17;
    private static final int DEFAULT_MULTIPLIER = 37;

    private int hashCode ;
    private int count;
    private int multiplier;

    /**
     * 构造函数
     */
    public CacheKey(){
        this.hashCode = DEFAULT_HASHCODE;
        this.count = 0;
        this.multiplier = DEFAULT_MULTIPLIER;
    }

    /**
     * 返回CacheKey值
     * @return
     */
    public int getCode(){
        return this.hashCode;
    }

    /**
     * 计算CacheKey中的HashCode
     * @param object
     */
    public void update(Object object){
        int baseHashCode = object == null ? 1 : object.hashCode();
        count++;
        baseHashCode *= count;
        hashCode =multiplier * hashCode + baseHashCode;
    }
}
