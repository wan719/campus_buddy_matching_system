package cn.edu.swu.campus_buddy_matching_system.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / OpenAPI 全局配置
 */
/**
 * SpringDoc配置类，用于配置OpenAPI文档
 * 该类用于生成和定制API文档，包括标题、描述、版本等信息，以及安全方案配置
 */
@Configuration
public class SpringDocConfig {

    /**
     * 创建并配置OpenAPI Bean
     * 
     * @return 自定义的OpenAPI实例，包含API文档的基本信息和安全配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // 定义安全方案的名称
        final String securitySchemeName = "bearerAuth";

        // 返回一个自定义的OpenAPI实例
        return new OpenAPI()
                // 配置API文档的基本信息
                .info(new Info()
                        .title("校园搭子匹配系统 API 文档") // API文档标题
                        .description("基于 Spring Boot + Spring Security + JWT 的校园搭子匹配系统接口文档") // API文档描述
                        .version("v2.0.0") // API版本号
                        .contact(new Contact()
                                .name("西南大学") // 联系人名称
                                .email("li20060719@email.swu.edu.cn") // 联系人邮箱
                                .url("https://www.swu.edu.cn"))) // 联系人网址

                // 添加安全需求
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 配置组件，包括安全方案
                .components(new Components()
                        // 添加安全方案
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName) // 安全方案名称
                                        .type(SecurityScheme.Type.HTTP) // 安全方案类型为HTTP
                                        .scheme("bearer") // 认证方案为Bearer
                                        .bearerFormat("JWT") // Bearer格式为JWT
                                        .description("请输入 JWT Token，本系统会自动拼接 Bearer 前缀"))); // 安全方案描述
    }
}