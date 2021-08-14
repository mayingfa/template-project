package top.qiudb.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import top.qiudb.enums.ResultCode;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 19:01
 * @description 自定义统一响应体
 */

@Getter
@Data
@ApiModel("统一响应体")
public class ResultVO<T> {
    @ApiModelProperty(value = "状态码", notes = "默认200成功")
    private Integer code;
    @ApiModelProperty(value = "响应信息", notes = "说明响应情况")
    private String msg;
    @ApiModelProperty(value = "响应的具体数据")
    private T data;

    public ResultVO(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public ResultVO(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMsg();
        this.data = data;
    }
}