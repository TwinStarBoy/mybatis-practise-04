package com.gupaoedu.mybatis.v2.Interceptor;

import com.gupaoedu.mybatis.v2.annotation.Intercepts;
import com.gupaoedu.mybatis.v2.plugin.Interceptor;
import com.gupaoedu.mybatis.v2.plugin.Invovation;
import com.gupaoedu.mybatis.v2.plugin.Plugin;

import java.util.Arrays;
import java.util.Date;

/**
 * 自定义插件
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {
    @Override
    public Object interceptor(Invovation invovation) throws Throwable {
        String statement = (String) invovation.getArgs()[0];
        Object[] parameter = (Object[]) invovation.getArgs()[1];
        Class pojo = (Class) invovation.getArgs()[2];

        System.out.println("插件输出:SQL:[" + statement +"]");
        System.out.println("插件输出:Parameters: " + Arrays.toString(parameter));

        Long starttime = System.currentTimeMillis();
        System.out.println("sql执行开始时间:" + new Date(starttime));
        Object object = invovation.proceed();
        Long endtime = System.currentTimeMillis();
        System.out.println("sql执行结束时间:" + new Date(endtime));
        System.out.println("耗时:" + (endtime - starttime) + "毫秒");
        return invovation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }
}
