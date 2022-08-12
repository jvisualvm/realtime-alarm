package com.risen.realtime.framework.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/3/7 17:40
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class PushKnife4jConfig {

    @Bean
    public Docket createPushManagerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("推送中心-系统缓存管理接口")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.risen.realtime.framework.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("推送管理接口")
                .contact(new Contact("", "", ""))
                .version("v1.1.0")
                .title("API接口文档")
                .build();
    }

}
