package top.qiudb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 19:45
 * @description 自定义参数校验错误码和错误信息注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD}) // 表明该注解只能放在类的字段上
public @interface ExceptionCode {
    // 响应码 code
    int value() default 401;
    // 响应信息 msg
    String message() default  "参数校验错误";
}
