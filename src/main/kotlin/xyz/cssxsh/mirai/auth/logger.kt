package xyz.cssxsh.mirai.auth

import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.admin.*

internal val logger by lazy {
    try {
        MiraiAuthenticatorPlugin.logger
    } catch (_: ExceptionInInitializerError) {
        MiraiLogger.Factory.create(MiraiAdministrator::class)
    }
}