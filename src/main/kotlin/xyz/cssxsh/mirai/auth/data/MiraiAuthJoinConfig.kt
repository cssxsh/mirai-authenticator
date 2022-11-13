package xyz.cssxsh.mirai.auth.data

import net.mamoe.mirai.console.data.*

@PublishedApi
internal object MiraiAuthJoinConfig: AutoSavePluginConfig("join") {
    @ValueName("timeout")
    @ValueDescription("等待问题提交时间")
    var timeout: Long by value(30_000L)

    @ValueName("count")
    @ValueDescription("问题允许的回答次数")
    var count: Int by value(3)

    @ValueName("checkers")
    @ValueDescription("检查内容配置")
    val checkers: MutableMap<Long, List<String>> by value()

    @ValueName("validators")
    @ValueDescription("验证内容配置")
    val validators: MutableMap<Long, List<String>> by value()

    @ValueName("official")
    @ValueDescription("官方机器人ID, 会自动放行")
    val official: MutableSet<Long> by value {
        add(2854196301)
        add(2854196306)
        add(2854196310)
    }
}