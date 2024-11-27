package xyz.cssxsh.mirai.auth

import net.mamoe.mirai.utils.*

internal val logger by lazy {
    try {
        MiraiAuthenticatorPlugin.logger
    } catch (_: Throwable) {
        MiraiLogger.Factory.create(MiraiAuthenticator::class)
    }
}