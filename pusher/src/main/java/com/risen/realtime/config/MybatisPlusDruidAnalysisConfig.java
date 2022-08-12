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
@ConditionalOnProperty(prefix = "spring.datasource", name = "czreport.url")
@Configuration
@MapperScan(basePackages = "com.risen.realtime.resource.report.analysis.mapper", sqlSessionTemplateRef = "czReport")
public class MybatisPlusDruidAnalysisConfig {

    /**********************************************************滁州报表配置************************************************/

    @Bean("czReportSqlSessionFactory")
    public SqlSessionFactory czReportSqlSessionFactory(@Qualifier("czReportDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:mapper/czreport/*Mapper.xml"));
        SqlSessionFactory sqlSession = sqlSessionFactory.getObject();
        sqlSession.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSession;
    }


    @Bean(name = "czReportTransactionManager")
    public DataSourceTransactionManager czReportTransactionManager(@Qualifier("czReportDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "czReport")
    public SqlSessionTemplate czReportSqlSessionTemplate(@Qualifier("czReportSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**********************************************************滁州报表配置************************************************/


}




