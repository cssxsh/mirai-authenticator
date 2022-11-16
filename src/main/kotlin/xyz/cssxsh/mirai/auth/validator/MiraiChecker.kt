package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import javax.script.*

/**
 * 校验接口
 */
public interface MiraiChecker {
    public val logger: MiraiLogger

    /**
     * 检查
     */
    public suspend fun check(event: MemberJoinRequestEvent): Boolean

    /**
     * 装入基本 Bindings
     */
    public fun <T: Bindings> T.apply(event: MemberJoinRequestEvent): T = apply {
        this["bot"] = event.bot
        this["logger"] = logger
        this["eventId"] = event.eventId
        this["fromId"] = event.fromId
        this["fromNick"] = event.fromNick
        this["groupId"] = event.groupId
        this["groupName"] = event.groupName
        this["message"] = event.message
        this["invitorId"] = event.invitorId
    }

    public companion object {
        @PublishedApi
        internal val providers: MutableMap<String, () -> MiraiChecker> = HashMap()

        init {
            providers["question"] = ::MiraiQuestionChecker
            providers["profile"] = ::MiraiProfileChecker
            providers["bilibili"] = ::MiraiGuardChecker
        }

        /**
         * 构建一个指定 id 的校验器
         */
        public operator fun invoke(id: String): MiraiChecker {
            val block = providers[id] ?: throw NoSuchElementException("MiraiChecker $id")
            return block()
        }

        /**
         * 设置一个新的校验器
         */
        public operator fun set(id: String, provider: () -> MiraiChecker) {
            providers[id] = provider
        }

        internal val QA: Regex = """(?:问题|答案)：(.+)""".toRegex()
    }
}