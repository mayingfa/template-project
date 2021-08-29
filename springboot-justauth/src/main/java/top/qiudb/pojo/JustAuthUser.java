package top.qiudb.pojo;

import lombok.ToString;
import me.zhyd.oauth.model.AuthUser;

import java.io.Serializable;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/29 12:59
 * @description 授权用户信息
 */

@ToString
public class JustAuthUser extends AuthUser implements Serializable {
    /**
     * 自定义构造函数
     *
     * @param authUser 授权成功后的用户信息，根据授权平台的不同，获取的数据完整性也不同
     */
    public JustAuthUser(AuthUser authUser) {
        super(authUser.getUuid(), authUser.getUsername(), authUser.getNickname(), authUser.getAvatar(),
                authUser.getBlog(), authUser.getCompany(), authUser.getLocation(), authUser.getEmail(),
                authUser.getRemark(), authUser.getGender(), authUser.getSource(), authUser.getToken(),
                authUser.getRawUserInfo());
    }
}
