package com.gupaoedu.mybatis;

import com.gupaoedu.mybatis.v2.mapper.Blog;
import com.gupaoedu.mybatis.v2.mapper.BlogMapper;
import com.gupaoedu.mybatis.v2.session.DefaultSqlSession;
import com.gupaoedu.mybatis.v2.session.SqlSessionFactory;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class TestMybatis {
    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactory();

        DefaultSqlSession sqlSession = sqlSessionFactory.build().openSqlSession();

        //获取代理类
        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);

        Blog blog = blogMapper.selectBlogById(1);

        System.out.println("第一次查询:" + blog);

        blog = blogMapper.selectBlogById(1);

        System.out.println("第二次查询:" + blog);


    }
}
