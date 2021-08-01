package top.qiudb.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.qiudb.entity.User;
import top.qiudb.service.UserService;
import top.qiudb.vo.ResultVO;
import javax.validation.Valid;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:15
 * @description 用户接口
 */

@RestController
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation("添加用户")
    @PostMapping("/user")
    public ResultVO<String> addUser(@RequestBody @Valid User user) {
        return new ResultVO<>(userService.addUser(user));
    }

    @ApiOperation("查询用户")
    @GetMapping("/user")
    public User getUser() {
        return userService.findUser();
    }
}
