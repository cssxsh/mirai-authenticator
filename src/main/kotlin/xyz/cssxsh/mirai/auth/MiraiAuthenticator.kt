package xyz.cssxsh.mirai.auth

import kotlinx.coroutines.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.*
import kotlin.coroutines.*

public object MiraiAuthenticator : SimpleListenerHost() {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        when (exception) {
            is CancellationException -> {
                // ...
            }
            is ExceptionInEventHandlerException -> {
                logger.warning({ "MiraiAuthenticator with ${exception.event}" }, exception.cause)
            }
            else -> {
                logger.warning({ "MiraiAuthenticator" }, exception)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    internal suspend fun MemberJoinRequestEvent.handle() {
        when (auth(this)) {
            MiraiAuthStatus.PASS -> accept()
            MiraiAuthStatus.FAIL -> reject(blackList = false, message = "验证失败")
            MiraiAuthStatus.BLACK -> reject(blackList = true, message = "验证失败")
            MiraiAuthStatus.IGNORE -> return
        }
        intercept()
    }

    @EventHandler(priority = EventPriority.HIGH)
    internal suspend fun MemberJoinEvent.handle() {
        when (auth(this)) {
            MiraiAuthStatus.PASS, MiraiAuthStatus.IGNORE -> return
            MiraiAuthStatus.FAIL -> member.kick(block = false, message = "验证失败")
            MiraiAuthStatus.BLACK -> member.kick(block = true, message = "验证失败")
        }
        intercept()
    }

    public suspend fun auth(event: MemberJoinRequestEvent): MiraiAuthStatus {
        val question = event.message.substringAfter("问题：").substringBefore('\n')
        val answer = event.message.substringAfterLast("答案：")

        return MiraiAuthStatus.IGNORE
    }

    public suspend fun auth(event: MemberJoinEvent): MiraiAuthStatus {
        // 官方机器人 listOf("2854196310", "2854196306")
        event.group.sendMessage(At(event.member) + ".....")
        event.group.sendMessage(At(event.member) + ".....")

        var count = 3
        while (count-- > 0) {
            val response = globalEventChannel().nextEvent<MessageEvent>(EventPriority.HIGH)
            response.intercept()

            val answer = response.message.contentToString()

            if (answer.isEmpty()) {
                TODO()
            }
        }

        return MiraiAuthStatus.IGNORE
    }
}