# Mirai Authenticator

> 基于 Mirai Console 的 加群/好友 验证插件 

[![Release](https://img.shields.io/github/v/release/cssxsh/mirai-authenticator)](https://github.com/cssxsh/mirai-authenticator/releases)
[![Downloads](https://img.shields.io/github/downloads/cssxsh/mirai-authenticator/total)](https://repo1.maven.org/maven2/xyz/cssxsh/mirai/mirai-authenticator/)
[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/mirai-authenticator)](https://search.maven.org/artifact/xyz.cssxsh.mirai/mirai-authenticator)

**使用前应该查阅的相关文档或项目**

*   [User Manual](https://github.com/mamoe/mirai/blob/dev/docs/UserManual.md)
*   [Permission Command](https://github.com/mamoe/mirai/blob/dev/mirai-console/docs/BuiltInCommands.md#permissioncommand)
*   [Chat Command](https://github.com/project-mirai/chat-command)

**目前只实现了加群验证的功能**

## MCL 指令安装

**请确认 mcl.jar 的版本是 2.1.0+**  
`./mcl --update-package xyz.cssxsh.mirai:mirai-authenticator --channel maven-stable --type plugin`

## 指令

### auth-join

*   `/auth-join check [group] {types}` 进群前检查  
    例如: `/auth-join check 123456 profile question`

*   `/auth-join validator [group] {types}` 进群后验证  
    例如: `/auth-join validator 123456 captcha`

*   `/auth-join official [id]` 设置自动放行的QQ号  
    例如: `/auth-join official 123456789`

*   `/auth-join timeout [mills]` 问题回答等待时间  
    例如: `/auth-join mills 180000`

*   `/auth-join count [value]` 问题允许回答次数  
    例如: `/auth-join count 5`

## 配置 Lua 校验脚本

自定义验证分别在以下文件夹中
*   `data/xyz.cssxsh.mirai.plugin.mirai-authenticator/profile`  
*   `data/xyz.cssxsh.mirai.plugin.mirai-authenticator/question`  

脚本文件名对应群号, 例如 `123456.lua`

`Global variable` (bindings) 支持的属性和方法有以下

*   `bot` bot 对象
*   `event_id` 事件id
*   `group_id` 群ID
*   `group_name` 群名
*   `message` 请求消息
*   `invitor_id` 邀请人
*   `from_id` 请求者ID
*   `from_nick` 请求者NICK

### Profile 校验脚本

对于 `Profile` 校验脚本, 还将支持

*   `from_profile` 请求者profile
    *   `getAge` 获取年龄
    *   `getQLevel` 获取QQ等级
    *   `getEmail` 获取右键
    *   `getNickname` 获取昵称

例如:  
```lua
return from_profile:getQLevel() > 4;
```

### Question 校验脚本

对于 `Question` 校验脚本, 还将支持

*   `question` 问题
*   `answer` 回答

例如:
```lua
return answer == "114514";
```
