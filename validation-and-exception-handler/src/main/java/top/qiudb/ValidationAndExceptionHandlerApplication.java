package top.qiudb;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:05
 */

@SpringBootApplication
@EnableKnife4j
public class ValidationAndExceptionHandlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ValidationAndExceptionHandlerApplication.class, args);
    }
}
