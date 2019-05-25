package com.gupaoedu.mybatis.v2.annotation;

import java.lang.annotation.*;

/**
 * 注解方法，配置sql语句
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
    String value();
}
