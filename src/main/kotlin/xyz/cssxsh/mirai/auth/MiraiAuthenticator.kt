package xyz.cssxsh.mirai.auth

import kotlinx.coroutines.*
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.auth.validator.*
import xyz.cssxsh.mirai.auth.data.*
import kotlin.coroutines.*

/**
 * 认证插件核心监听器
 */
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
        if ((group?.botPermission ?: MemberPermission.MEMBER) < MemberPermission.ADMINISTRATOR) return
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
        if (group.botPermission < MemberPermission.ADMINISTRATOR) return
        when (auth(this)) {
            MiraiAuthStatus.PASS -> return
            MiraiAuthStatus.FAIL -> member.kick(block = false, message = "验证失败")
            MiraiAuthStatus.BLACK -> member.kick(block = true, message = "验证失败")
            MiraiAuthStatus.IGNORE -> return
        }
        intercept()
    }

    private fun Group.checkers(): Sequence<MiraiChecker>? {
        val ids = MiraiAuthJoinConfig.checkers[id] ?: return null
        return sequence {
            for (id in ids) {
                val validator = try {
                    MiraiChecker(id)
                } catch (cause: Exception) {
                    logger.warning({ "$name 获取验证器 $id 失败" }, cause)
                    continue
                }
                yield(validator)
            }
        }
    }

    private fun Group.validators(): Sequence<MiraiValidator>? {
        val ids = MiraiAuthJoinConfig.validators[id] ?: return null
        return sequence {
            for (id in ids) {
                val validator = try {
                    MiraiValidator(id)
                } catch (cause: Exception) {
                    logger.warning({ "$name 获取验证器 $id 失败" }, cause)
                    continue
                }
                yield(validator)
            }
        }
    }

    /**
     * 加群前验证
     * @param event 被验证成员的申请事件
     */
    public suspend fun auth(event: MemberJoinRequestEvent): MiraiAuthStatus {
        val group = event.group ?: return MiraiAuthStatus.IGNORE
        val checkers = group.checkers() ?: return MiraiAuthStatus.IGNORE

        for (checker in checkers) {
            try {
                if (checker.check(event = event)) return MiraiAuthStatus.PASS
            } catch (cause: IllegalStateException) {
                logger.warning({ "提交<验证答案>失败" }, cause)
            }
        }

        return MiraiAuthStatus.FAIL
    }

    /**
     * 加群后验证
     * @param event 被验证成员的入群事件
     * @see MiraiValidator
     */
    public suspend fun auth(event: MemberJoinEvent): MiraiAuthStatus {
        if (event.member.id in MiraiAuthJoinConfig.official) return MiraiAuthStatus.PASS
        val validators = event.group.validators() ?: return MiraiAuthStatus.IGNORE

        for (validator in validators) {
            var count = MiraiAuthJoinConfig.count
            while (count-- > 0) {
                val question = try {
                    validator.question(event = event)
                } catch (cause: IllegalStateException) {
                    logger.warning({ "生成<验证问题>失败" }, cause)
                    continue
                }
                try {
                    event.group.sendMessage(message = At(event.member) + question)
                } catch (cause: IllegalStateException) {
                    logger.warning({ "发送<验证问题>失败" }, cause)
                    continue
                }
                val response = withTimeoutOrNull(MiraiAuthJoinConfig.timeout) {
                    globalEventChannel().nextEvent<GroupMessageEvent>(priority = EventPriority.HIGH, intercept = true) {
                        it.group == event.group && it.sender == event.member
                    }
                } ?: kotlin.run {
                    try {
                        event.group.sendMessage(message = At(event.member) + "回答超时")
                    } catch (cause: IllegalStateException) {
                        logger.warning({ "发送<回答超时>失败" }, cause)
                    }
                    return MiraiAuthStatus.FAIL
                }

                val content = response.message.contentToString()

                try {
                    return if (validator.auth(answer = content)) MiraiAuthStatus.PASS else continue
                } catch (cause: IllegalStateException) {
                    logger.warning({ "提交<验证答案>失败" }, cause)
                }
            }
        }

        return MiraiAuthStatus.FAIL
    }
}