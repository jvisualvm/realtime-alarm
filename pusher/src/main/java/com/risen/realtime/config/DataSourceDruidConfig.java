package com.risen.realtime.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/4/12 11:02
 */

@Configuration
@ConditionalOnProperty(prefix = "spring.datasource", name = "master.url")
public class DataSourceDruidConfig {


    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Bean("mainDruidDataSource")
    public DruidDataSource czReportDataSource() {
        return new DruidDataSource();
    }


    /**********************************************************quartz配置************************************************/
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
    @Bean("quartzDataSourceProperties")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "quartzDataSource")
    @QuartzDataSource
    public DataSource quartzDataSource(@Qualifier("mainDruidDataSource") DruidDataSource dataSource, @Qualifier("quartzDataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource masterDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dataSource, masterDataSource);
        BeanUtils.copyProperties(dataSourceProperties, masterDataSource);
        return masterDataSource;
    }
    /**********************************************************quartz配置************************************************/


    /**********************************************************推送任务配置************************************************/
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Bean("pushDataSourceProperties")
    public DataSourceProperties pushDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "pushDataSource")
    @Primary
    public DataSource pushDataSource(@Qualifier("mainDruidDataSource") DruidDataSource dataSource, @Qualifier("pushDataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource masterDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dataSource, masterDataSource);
        BeanUtils.copyProperties(dataSourceProperties, masterDataSource);
        return masterDataSource;
    }
    /**********************************************************推送任务配置************************************************/


    /**********************************************************滁州报表配置************************************************/
    @ConfigurationProperties(prefix = "spring.datasource.czreport")
    @Bean("czReportDataSourceProperties")
    public DataSourceProperties czReportDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "czReportDataSource")
    public DataSource czReportDataSource(@Qualifier("mainDruidDataSource") DruidDataSource dataSource, @Qualifier("czReportDataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource masterDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dataSource, masterDataSource);
        BeanUtils.copyProperties(dataSourceProperties, masterDataSource);
        return masterDataSource;
    }
    /**********************************************************滁州报表配置************************************************/


    /**********************************************************zeus配置************************************************/
    @ConfigurationProperties(prefix = "spring.datasource.zeus")
    @Bean("zeusDataSourceProperties")
    public DataSourceProperties zeusSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "zeusDataSource")
    public DataSource zeusDataSource(@Qualifier("mainDruidDataSource") DruidDataSource dataSource, @Qualifier("zeusDataSourceProperties") DataSourceProperties dataSourceProperties) {
        DruidDataSource masterDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dataSource, masterDataSource);
        BeanUtils.copyProperties(dataSourceProperties, masterDataSource);
        return masterDataSource;
    }
    /**********************************************************zeus配置************************************************/


}
