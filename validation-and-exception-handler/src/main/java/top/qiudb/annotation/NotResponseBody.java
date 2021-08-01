package top.qiudb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 19:46
 * @description 防止数据统一响应处理
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD}) // 表明该注解只能放在方法上
public @interface NotResponseBody {
}
