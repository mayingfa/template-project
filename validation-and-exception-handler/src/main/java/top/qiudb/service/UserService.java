package top.qiudb.service;

import top.qiudb.entity.User;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/7/31 18:18
 * @description 用户业务接口
 */
public interface UserService {
    /**
     * @param user 用户对象
     * @return 成功则返回"success"，失败则返回错误信息
     */
    String addUser(User user);

    /**
     * @return 成功则返回用户信息，失败则返回错误信息
     */
    User findUser();
}
