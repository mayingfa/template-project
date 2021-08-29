package top.qiudb.controller;

import com.alibaba.fastjson.JSON;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.qiudb.pojo.JustAuthUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 马英发
 * @email qiudb.top@aliyun.com
 * @date 2021/8/29 11:40
 * @description 第三方登录
 */

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OauthController {
    private final AuthRequestFactory factory;

    @Value("${justauth.login}")
    private String login;

    /**
     * 登录类型
     *
     * @return 登录类型列表
     */
    @GetMapping
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(Collectors.toMap(oauth -> oauth.toLowerCase() + "登录", oauth -> login + oauth.toLowerCase()));
    }

    /**
     * 执行登录请求
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException IO异常
     */
    @RequestMapping("/login/{oauthType}")
    public void renderAuth(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(oauthType.toUpperCase());
        response.sendRedirect(authRequest.authorize(AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return 登录成功后的信息
     */
    @RequestMapping("/callback/{oauthType}")
    public AuthResponse login(@PathVariable String oauthType, AuthCallback callback) {
        AuthRequest authRequest = factory.get(oauthType);
        AuthResponse<JustAuthUser> response = authRequest.login(callback);
        log.info("【response】= {}", JSON.toJSONString(response));
        if (response.ok()) {
            JustAuthUser justAuthUser = new JustAuthUser(response.getData());
            log.info("【uuid】= {}", justAuthUser.getUuid());
            log.info("【username】= {}", justAuthUser.getUsername());
            log.info("【nickname】= {}", justAuthUser.getNickname());
            log.info("【avatar】= {}", justAuthUser.getAvatar());
            log.info("【accessToken】= {}", justAuthUser.getToken().getAccessToken());
        }
        return response;
    }
}
