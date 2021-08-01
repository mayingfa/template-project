package top.qiudb.service;

import org.springframework.stereotype.Service;
import top.qiudb.entity.User;
import top.qiudb.exception.ApiException;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:19
 * @description 用户业务实现类
 */

@Service
public class UserServiceImpl implements UserService{
    @Override
    public String addUser(User user) {
        // 直接编写业务逻辑
        if ("1325554003".equals(user.getAccount())) {
            throw new ApiException("用户已存在");
        }
        return "添加成功";
    }

    @Override
    public User findUser() {
        User user = new User();
        user.setId(1L);
        user.setAccount("1325554003");
        user.setPassword("123456");
        user.setEmail("1325554003@qq.com");
        return user;
    }
}
