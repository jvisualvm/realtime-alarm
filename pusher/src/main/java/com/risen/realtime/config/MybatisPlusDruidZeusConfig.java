package com.risen.realtime.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/12 11:09
 */
@ConditionalOnProperty(prefix = "spring.datasource", name = "zeus.url")
@Configuration
@MapperScan(basePackages = "com.risen.realtime.resource.report.zeus.mapper", sqlSessionTemplateRef = "zeusSqlRef")
public class MybatisPlusDruidZeusConfig {

    /**********************************************************zeus配置************************************************/
    @Bean("zeusSqlSessionFactory")
    public SqlSessionFactory zeusSqlSessionFactory(@Qualifier("zeusDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:mapper/zeus/*Mapper.xml"));
        SqlSessionFactory sqlSession = sqlSessionFactory.getObject();
        sqlSession.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSession;
    }


    @Bean(name = "zeusTransactionManager")
    public DataSourceTransactionManager zeusTransactionManager(@Qualifier("zeusDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "zeusSqlRef")
    public SqlSessionTemplate zeusSqlSessionTemplate(@Qualifier("zeusSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    /**********************************************************zeus配置************************************************/


}




