package top.qiudb.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.InetAddress;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/1 9:56
 * @description swagger配置类
 */

@Configuration
@EnableOpenApi
@Slf4j
public class Swagger3Config {
    /**
     * 是否启用swagger文档
     */
    @Value("${swagger.enable}")
    private boolean enable;
    @Value("${server.port}")
    private int port;
    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    @SneakyThrows
    public Docket createRestApi() {
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        Docket docket = new Docket(DocumentationType.OAS_30)
                .enable(enable)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.qiudb.controller"))
                .paths(PathSelectors.any())
                .build();

        // 控制台输出Knife4j增强接口文档地址
        log.info("Knife4j增强接口文档地址: http://{}:{}{}/doc.html", ipAddress, port, contextPath);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Elasticsearch实战")
                .description("Springboot整合Elasticsearch实现CRUD")
                .termsOfServiceUrl("https://gitee.com/ma_ying_fa")
                .contact(new Contact("马英发", "https://gitee.com/ma_ying_fa", "qiudb.top@aliyun.com"))
                .version("1.0")
                .build();
    }
}