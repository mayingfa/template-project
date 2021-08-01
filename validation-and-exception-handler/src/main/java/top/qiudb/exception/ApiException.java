package top.qiudb.exception;

import lombok.Getter;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:59
 * @description 自定义异常
 */
@Getter
public class ApiException extends RuntimeException {
    private final String msg;

    public ApiException(String msg) {
        this.msg = msg;
    }
}
