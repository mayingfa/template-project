## 项目介绍

> JustAuth，如你所见，它仅仅是一个**第三方授权登录**的**工具类库**，它可以让我们脱离繁琐的第三方登录SDK，让登录变得**So easy!**

项目使用 SpringBoot整合JustAuth，完成第三方登录操作，目前项目已实现如下登录方式

- 钉钉授权登录
- 百度授权登录
- 华为授权登录
- gitee授权登录

项目启动后，访问控制台输出的项目地址，通过授权链接执行授权操作，授权成功后会执行回调地址

## JustAuth 项目

JustAuth帮助文档：https://justauth.wiki/

Gitee地址：https://gitee.com/yadong.zhang/JustAuth/

Spring Boot 快速集成第三方登录功能：https://xkcoding.com/2019/05/22/spring-boot-login-with-oauth.html

vue + justauth 实现前后端分离下的第三方登录：https://blog.csdn.net/qq_40663357/article/details/101231252

## JustAuth与现有用户系统整合

JustAuth与用户系统整合流程图

![JustAuth与现有用户系统整合](https://oss-qiu.oss-cn-hangzhou.aliyuncs.com/qiu-blogs-typecho/20210829160323.png)

**数据库表结构（参考）**

以下为 **第三方登录 + 本地用户系统** 的数据库表结构（仅供参考）

[#](https://justauth.wiki/ext/justauth_integrated_with_the_existing_account_system.html#用户表-user)用户表（user）

|   字段   |  类型   |   释义   | NULL  |                          备注                          |
| :------: | :-----: | :------: | :---: | :----------------------------------------------------: |
|    id    |   int   |   主键   | false |                                                        |
| username | varchar |  用户名  | false |                                                        |
| password | varchar |   密码   | true  | 选择使用第三方用户登录时不存在密码，除非进行了用户绑定 |
| nickname | varchar |   昵称   | true  |                                                        |
|  gender  | varchar |   性别   | true  |                                                        |
|  avatar  | varchar |   头像   | true  |                                                        |
|   blog   | varchar | 个人地址 | true  |                                                        |
| company  | varchar |  公司名  | true  |                                                        |
| location | varchar |   地址   | true  |                                                        |
|  email   | varchar |   邮箱   | true  |                                                        |

 [#](https://justauth.wiki/ext/justauth_integrated_with_the_existing_account_system.html#社会化用户表-social-user)社会化用户表（social_user）

|        字段        |  类型   |             释义             | NULL  |                             备注                             |
| :----------------: | :-----: | :--------------------------: | :---: | :----------------------------------------------------------: |
|         id         |   int   |             主键             | false |                                                              |
|        uuid        | varchar |      第三方系统的唯一ID      | false | 详细解释请参考：[名词解释](https://justauth.wiki/ext/quickstart/explain.md?id=justauth中的关键词) |
|       source       | varchar |        第三方用户来源        | false | GITHUB、GITEE、QQ，更多请参考：[AuthDefaultSource.java(opens new window)](https://github.com/justauth/JustAuth/blob/master/src/main/java/me/zhyd/oauth/config/AuthDefaultSource.java) |
|    access_token    | varchar |        用户的授权令牌        | false |                                                              |
|     expire_in      |   int   | 第三方用户的授权令牌的有效期 | true  |                       部分平台可能没有                       |
|   refresh_token    | varchar |           刷新令牌           | true  |                       部分平台可能没有                       |
|      open_id       | varchar |     第三方用户的 open id     | true  |                       部分平台可能没有                       |
|        uid         | varchar |       第三方用户的 ID        | true  |                       部分平台可能没有                       |
|    access_code     | varchar |      个别平台的授权信息      | true  |                       部分平台可能没有                       |
|      union_id      | varchar |    第三方用户的 union id     | true  |                       部分平台可能没有                       |
|       scope        | varchar |     第三方用户授予的权限     | true  |                       部分平台可能没有                       |
|     token_type     | varchar |      个别平台的授权信息      | true  |                       部分平台可能没有                       |
|      id_token      | varchar |           id token           | true  |                       部分平台可能没有                       |
|   mac_algorithm    | varchar |    小米平台用户的附带属性    | true  |                       部分平台可能没有                       |
|      mac_key       | varchar |    小米平台用户的附带属性    | true  |                       部分平台可能没有                       |
|        code        | varchar |        用户的授权code        | true  |                       部分平台可能没有                       |
|    oauth_token     | varchar |  Twitter平台用户的附带属性   | true  |                       部分平台可能没有                       |
| oauth_token_secret | varchar |  Twitter平台用户的附带属性   | true  |                       部分平台可能没有                       |

[#](https://justauth.wiki/ext/justauth_integrated_with_the_existing_account_system.html#社会化用户-系统用户关系表-social-user-auth)社会化用户 & 系统用户关系表（social_user_auth）

|      字段      |  类型   |     释义     | NULL  |              备注              |
| :------------: | :-----: | :----------: | :---: | :----------------------------: |
|       id       |   int   |     主键     | false |                                |
|    user_id     | varchar |  系统用户ID  | false |                                |
| social_user_id | varchar | 社会化用户ID | false | 数据库主键（非第三方用户的ID） |

> 注意

1. 建议通过`uuid` + `source`的方式唯一确定一个用户，这样可以解决用户身份归属的问题。因为 单个用户ID 在某一平台中是唯一的，但不能保证在所有平台中都是唯一的。

2. 相关SQL操作的伪代码

   1. 获取第三方平台 `GITHUB` 用户（UUID = `xxxxxxx`）的 `SQL` 语句伪代码

      ```sql
      SELECT
          su.* 
      FROM
          `social_user` su 
      WHERE
          su.uuid = 'xxxxxxx' 
          AND su.source = 'GITHUB'
      ```

   2. 查询系统用户（ID = `1`）是否绑定了 `GITHUB` 平台账号的 `SQL` 语句伪代码：

      ```sql
      SELECT
          count(1)
      FROM
          `social_user_auth` sua
      INNER JOIN `social_user` su ON sua.social_user_id = su.id
      WHERE
          sua.user_id = '1' 
          AND su.source = 'GITHUB'
      ```

   3. 解绑 `GITHUB` 平台的绑定账号

      ```sql
      DELETE FROM `social_user_auth` sua WHERE sua.social_user_id = '1' AND sua.user_id = '1'
      ```

      