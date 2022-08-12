package com.risen.realtime.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/12 11:09
 */
@ConditionalOnProperty(prefix = "spring.datasource", name = "master.url")
@Configuration
@MapperScan(basePackages = "com.risen.realtime.framework.mapper", sqlSessionTemplateRef = "pushSqlRef")
public class MybatisPlusDruidMasterConfig {

    /**********************************************************推送任务配置************************************************/
    @Bean("pushSqlSessionFactory")
    @Primary
    public SqlSessionFactory pushSqlSessionFactory(@Qualifier("pushDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:mapper/push/*Mapper.xml"));
        SqlSessionFactory sqlSession = sqlSessionFactory.getObject();
        sqlSession.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSession;
    }


    @Bean(name = "pushTransactionManager")
    @Primary
    public DataSourceTransactionManager pushTransactionManager(@Qualifier("pushDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "pushSqlRef")
    @Primary
    public SqlSessionTemplate pushSqlSessionTemplate(@Qualifier("pushSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    /**********************************************************推送任务配置************************************************/


}




