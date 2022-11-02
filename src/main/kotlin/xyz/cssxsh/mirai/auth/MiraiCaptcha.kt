package xyz.cssxsh.mirai.auth

import net.mamoe.mirai.message.data.*

public interface MiraiCaptcha {
    public val question: Message
    public suspend fun auth(answer: String): Boolean
}