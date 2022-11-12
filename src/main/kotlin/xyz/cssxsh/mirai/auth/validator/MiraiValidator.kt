package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*

/**
 * 验证接口
 * @see MiraiCaptchaValidator
 */
public interface MiraiValidator {
    /**
     * 获取问题, 重复访问等于刷新状态
     */
    public suspend fun question(event: MemberJoinEvent): Message

    /**
     * 提交答案
     */
    public suspend fun auth(answer: String): Boolean

    public companion object {
        @PublishedApi
        internal val providers: MutableMap<String, () -> MiraiValidator> = HashMap()

        init {
            providers["captcha"] = ::MiraiCaptchaValidator
        }

        /**
         * 构建一个指定 id 的验证器
         */
        public operator fun invoke(id: String): MiraiValidator {
            val block = providers[id] ?: throw NoSuchElementException("MiraiValidator ${id}.")
            return block()
        }

        /**
         * 设置一个新的验证器
         */
        public operator fun set(id: String, provider: () -> MiraiValidator) {
            providers[id] = provider
        }
    }
}