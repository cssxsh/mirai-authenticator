package xyz.cssxsh.mirai.auth.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.auth.*
import xyz.cssxsh.mirai.auth.data.*
import xyz.cssxsh.mirai.auth.validator.*

@PublishedApi
internal object MiraiAuthCaptchaCommand : SimpleCommand(
    owner = MiraiAuthenticatorPlugin,
    primaryName = "auth-captcha",
    description = "测试验证码"
) {

    @Handler
    suspend fun UserCommandSender.handle() {
        val validator = MiraiCaptchaValidator()
        val image = validator.getCaptchaImage().toExternalResource().use { resource ->
            subject.uploadImage(resource)
        }
        sendMessage(message = image + MiraiAuthJoinConfig.tip)
        val next = subject.bot.eventChannel
            .nextEvent<MessageEvent>(priority = EventPriority.HIGH, intercept = true) { it.sender == user }

        val answer = next.message.contentToString()

        val result = validator.verifyCaptcha(code = answer)

        sendMessage(message = "验证结果: ${result.code != -102} (${result.code})")
    }
}