package com.atguigu.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("OASystem-ApiDocument")
                .description("办公自动化系统应用程序接口文档")
                .version("1.0.0")
                .contact(new Contact("Qingchen Jia", "https://github.com/QingchenJia", "879484952@qq.com"))
                .build();
    }

    @Bean()
    public Docket authGroupApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("认证授权分组")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.atguigu.auth.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
