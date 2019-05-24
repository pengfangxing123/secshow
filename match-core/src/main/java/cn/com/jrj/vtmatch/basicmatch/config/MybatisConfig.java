package cn.com.jrj.vtmatch.basicmatch.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuan.cheng
 */
@Configuration
@ConditionalOnClass(SqlSessionDaoSupport.class)
@MapperScan(basePackages = "cn.com.jrj.vtmatch.basicmatch.matchcore.dao")
public class MybatisConfig {
}
