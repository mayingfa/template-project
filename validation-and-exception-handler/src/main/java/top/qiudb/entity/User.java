package top.qiudb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.qiudb.annotation.ExceptionCode;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:04
 * @description 用户实体类
 */

@Data
@ApiModel("用户")
public class User {
    @ApiModelProperty("用户id")
    @NotNull(message = "用户Id不能为空")
    private Long id;

    @ApiModelProperty("用户账号")
    @NotNull(message = "用户帐号不能为空")
    @Size(min = 6,max = 11,message = "帐号长度必须是6-11个字符")
    @ExceptionCode(value = 402, message = "账号验证错误")
    private String account;

    @ApiModelProperty("用户密码")
    @NotNull(message = "用户密码不能为空")
    @Pattern(regexp = "^[a-zA-Z]\\w{5,17}$", message = "密码要以字母开头，长度在6~18之间，只能包含字母、数字和下划线")
    @ExceptionCode(value = 403, message = "密码验证错误")
    private String password;

    @ApiModelProperty("用户邮箱")
    @NotNull(message = "用户邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
