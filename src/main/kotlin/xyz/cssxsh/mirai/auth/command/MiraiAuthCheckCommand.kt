package xyz.cssxsh.mirai.auth.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.auth.*
import xyz.cssxsh.mirai.auth.validator.*
import kotlin.random.*

@PublishedApi
internal object MiraiAuthCheckCommand : CompositeCommand(
    owner = MiraiAuthenticatorPlugin,
    primaryName = "auth-check",
    description = "测试校验器"
) {

    @SubCommand
    @Description("测试 question 校验器")
    suspend fun UserCommandSender.question(group: Long, question: String, answer: String) {
        @OptIn(MiraiInternalApi::class)
        val request = MemberJoinRequestEvent(
            bot = bot,
            eventId = Random.Default.nextLong(),
            message = """
                问题：$question
                答案：$answer
            """.trimIndent(),
            fromId = 0,
            fromNick = "",
            groupId = group,
            groupName = "",
            invitorId = user.id
        )

        val checker = MiraiQuestionChecker()
        val result = checker.check(event = request)

        sendMessage(message = "验证结果: $result")
    }

    @SubCommand
    @Description("测试 profile 校验器")
    suspend fun UserCommandSender.profile(group: Long, target: Long) {
        @OptIn(MiraiInternalApi::class)
        val request = MemberJoinRequestEvent(
            bot = bot,
            eventId = Random.Default.nextLong(),
            message = "",
            fromId = target,
            fromNick = "user.nick",
            groupId = group,
            groupName = "",
            invitorId = user.id
        )

        val checker = MiraiProfileChecker()
        val result = checker.check(event = request)

        sendMessage(message = "验证结果: $result")
    }
}