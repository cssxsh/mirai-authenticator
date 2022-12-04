package xyz.cssxsh.mirai.auth

import net.mamoe.mirai.utils.*

internal val logger by lazy {
    try {
        MiraiAuthenticatorPlugin.logger
    } catch (_: UninitializedPropertyAccessException) {
        MiraiLogger.Factory.create(MiraiAuthenticator::class)
    }
}