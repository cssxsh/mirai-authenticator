package xyz.cssxsh.mirai.auth.command

import net.mamoe.mirai.*
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.auth.*
import xyz.cssxsh.mirai.auth.validator.*
import kotlin.random.*

@PublishedApi
@OptIn(MiraiInternalApi::class)
internal object MiraiAuthCheckCommand : CompositeCommand(
    owner = MiraiAuthenticatorPlugin,
    primaryName = "auth-check",
    description = "测试校验器"
) {

    @SubCommand
    @Description("测试 question 校验器")
    suspend fun UserCommandSender.question(group: Long, question: String, answer: String) {
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
        val request = MemberJoinRequestEvent(
            bot = bot,
            eventId = Random.Default.nextLong(),
            message = "",
            fromId = target,
            fromNick = "",
            groupId = group,
            groupName = "",
            invitorId = user.id
        )

        sendMessage("${target}-${Mirai.queryProfile(bot, target)}")
        val checker = MiraiProfileChecker()
        val result = checker.check(event = request)

        sendMessage(message = "验证结果: $result")
    }

    @SubCommand
    @Description("测试 bilibili 校验器")
    suspend fun UserCommandSender.bilibili(group: Long, uid: Long) {
        val request = MemberJoinRequestEvent(
            bot = bot,
            eventId = Random.Default.nextLong(),
            message = """
                问题：请输入你的UID, (注意挂上舰长粉丝牌)
                答案：$uid
            """.trimIndent(),
            fromId = 0,
            fromNick = "",
            groupId = group,
            groupName = "",
            invitorId = user.id
        )

        val checker = MiraiGuardChecker()
        val result = checker.check(event = request)

        sendMessage(message = "验证结果: $result")
    }

    @SubCommand
    @Description("测试 afdian 校验器")
    suspend fun UserCommandSender.afdian(group: Long, uid: String) {
        val request = MemberJoinRequestEvent(
            bot = bot,
            eventId = Random.Default.nextLong(),
            message = """
                问题：请输入你的爱发电 ID
                答案：$uid
            """.trimIndent(),
            fromId = 0,
            fromNick = "",
            groupId = group,
            groupName = "",
            invitorId = user.id
        )

        val checker = MiraiSponsorChecker()
        val result = checker.check(event = request)

        sendMessage(message = "验证结果: $result")
    }
}