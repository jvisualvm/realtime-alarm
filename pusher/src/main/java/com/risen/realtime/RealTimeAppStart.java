package com.risen.realtime;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.risen.helper.annotation.EnableOrderClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})
@MapperScan(value = "com.risen.*")
@EnableKnife4j
@EnableSwagger2
@EnableTransactionManagement
@ComponentScan({"com.risen.*"})
@EnableOrderClient
@EnableAsync
public class RealTimeAppStart {

    public static void main(String[] args) {

        SpringApplication.run(RealTimeAppStart.class, args);

    }

}

