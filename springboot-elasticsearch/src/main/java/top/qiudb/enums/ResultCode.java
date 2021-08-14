package top.qiudb.enums;

import lombok.Getter;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:57
 * @description 响应码枚举
 */
@Getter
public enum ResultCode {
    /**
     * SUCCESS 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * FAILED 接口响应失败
     */
    FAILED(400, "操作失败"),

    /**
     * VALIDATE_FAILED 参数校验失败
     */
    VALIDATE_FAILED(401, "参数校验失败");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}