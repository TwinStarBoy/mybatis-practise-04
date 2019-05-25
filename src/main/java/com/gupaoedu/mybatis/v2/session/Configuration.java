package com.gupaoedu.mybatis.v2.session;

import com.gupaoedu.mybatis.TestMybatis;
import com.gupaoedu.mybatis.v2.annotation.Entity;
import com.gupaoedu.mybatis.v2.annotation.Select;
import com.gupaoedu.mybatis.v2.binding.MapperRegistry;
import com.gupaoedu.mybatis.v2.executor.CachingExecutor;
import com.gupaoedu.mybatis.v2.executor.Executor;
import com.gupaoedu.mybatis.v2.executor.SimpleExecutor;
import com.gupaoedu.mybatis.v2.plugin.Interceptor;
import com.gupaoedu.mybatis.v2.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Configuration {

    public static ResourceBundle sqlMappings;//SQL映射关系配置，使用注解时不用重复配置
    public static ResourceBundle properties;//全局配置
    public static MapperRegistry Mapper_Registry = new MapperRegistry();
    public static Map<String,String> mappedStatement = new HashMap<String,String>();

    private InterceptorChain interceptorChain = new InterceptorChain();
    public List<Class<?>> mapperList = new ArrayList<Class<?>>();//所有Mapper接口
    public List<String> classPaths = new ArrayList<String>();//所有类文件

    static {
        sqlMappings = ResourceBundle.getBundle("sql");
        properties = ResourceBundle.getBundle("mybatis");
    }

    /**
     * 初始化时解析全局配置文件
     */
    public Configuration(){
        //1.解析sql.properties
        for (String key : sqlMappings.keySet()){
            Class mapper = null;
            Class pojo = null;
            //properties中的value用--隔开，第一个sql语句
            String statement = sqlMappings.getString(key).split("--")[0];
            //properties中的value用--隔开，第二个是需要转换的POJO类型
            String pojoStr = sqlMappings.getString(key).split("--")[1];

            try {
                mapper = Class.forName(key.substring(0,key.lastIndexOf(".")));
                pojo = Class.forName(pojoStr);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Mapper_Registry.addMapper(mapper,pojo);
            mappedStatement.put(key,statement);
        }


        //2.解析Mapper接口配置，扫描注册
        String mapperPath = properties.getString("mapper.path");
        scanPackage(mapperPath);
        for (Class<?> mapper : mapperList){
            parsingClass(mapper);
        }

    }

    private void parsingClass(Class<?> mapper){
        //1.解析类上的注解
        //如果有@Entity注解，说明是查询数据库的接口
        if (mapper.isAnnotationPresent(Entity.class)){
            for (Annotation annotion : mapper.getAnnotations()){
                if (annotion.annotationType().equals(Entity.class)){
                    //注册接口和工厂类的映射关系,还有实体类的映射关系
                    Mapper_Registry.addMapper(mapper , ((Entity)annotion).value());
                }
            }
        }

        //2.解析方法上的注解
        Method[] methods = mapper.getMethods();
        for (Method method : methods){
            //解析Select注解的sql语句
            if (method.isAnnotationPresent(Select.class)){
                for (Annotation annotation : method.getDeclaredAnnotations()){
                    if (annotation.annotationType().equals(Select.class)){
                        //注册接口类型+方法名和sql语句的映射关系
                        String statement = method.getDeclaringClass().getName() + "." + method.getName();
                        mappedStatement.put(statement, ((Select)annotation).value());
                    }
                }
            }
        }

        //3.解析插件，可配置多个插件
        String pluginPathValue = properties.getString("plugin.path");
        String[] pluginPaths = pluginPathValue.split(",");
        if (pluginPaths != null){
            //将插件添加到InterceptorChain中
            for (String pluginPath : pluginPaths){
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(pluginPath).newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                interceptorChain.addInterceptor(interceptor);
            }
        }
    }

    private void scanPackage(String mapperPath){
        String classPath = TestMybatis.class.getResource("/").getPath();
        mapperPath = mapperPath.replace(".",File.separator);
        String mainPath = classPath + mapperPath;
        doPath(new File(mainPath));
        for (String className : classPaths){
            //TODO 这里要看下
//            className.replaceAll("")
            /**将classPath路径中的"/"换成"\",并将新路径的第一个"\",去掉
            * 即
            * /E:/gupaoxueyuan/mybatis/04/mybatiscustom/target/classes/
            * 换成
            * E:\gupaoxueyuan\mybatis\04\mybatiscustom\target\classes\
            * */
            String classPathTmp = classPath.replace("/","\\").replaceFirst("\\\\","");
            /**将className中的绝对路径去掉
             * 即
             * E:\gupaoxueyuan\mybatis\04\mybatiscustom\target\classes\com\gupaoedu\mybatis\v2\mapper\Blog.class
             * 换成
             * com\gupaoedu\mybatis\v2\mapper\Blog.class
             */
            className = className.replace(classPathTmp,"");//将绝对路径去掉
            /**
             *将className中的"\"换成"."
             * 即
             * com\gupaoedu\mybatis\v2\mapper\Blog.class
             * 换成
             * com.gupaoedu.mybatis.v2.mapper.Blog.class
             */
            className = className.replace("\\",".");
            /**
             * 将className中的".class"换成""
             * 即
             * com.gupaoedu.mybatis.v2.mapper.Blog.class
             * 换成
             * com.gupaoedu.mybatis.v2.mapper.Blog
             */
            className = className.replace(".class","");

            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz.isInterface()){
                mapperList.add(clazz);
            }
        }
    }


    private void doPath(File file){
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f1 : files){
                doPath(f1);
            }
        }else{
            if (file.getName().endsWith(".class")){
                classPaths.add(file.getPath());
            }
        }
    }
    /**
     * 根据statementName判断是否存在映射的sql
     * @param statementName
     * @return
     */
    public boolean hasStatement(String statementName){
        return mappedStatement.containsKey(statementName);
    }

    /**
     * 根据statementId获取sql
     * @return
     */
    public String getMapperStatemetn(String statementId){
        return mappedStatement.get(statementId);
    }

    public <T> T getMapper(Class<T> clazz , DefaultSqlSession sqlSession){
        return Mapper_Registry.getMapper(clazz,sqlSession);
    }
    /**
     *
     * @return
     */
    public Executor newExecutor(){
        Executor executor = null;

        if ("true".equals(properties.getString("cache.enabled"))){
            executor = new CachingExecutor(new SimpleExecutor());
        }else {
            executor = new SimpleExecutor();
        }

        //目前只拦截了Executor,所有的插件都对Executor进行代理，没有对拦截器类和方法签名进行判断
        if (interceptorChain.hasPlugin()){
            return (Executor) interceptorChain.pluginAll(executor);
        }

        return executor;

    }
}
