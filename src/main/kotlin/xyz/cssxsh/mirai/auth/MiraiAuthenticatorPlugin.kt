package xyz.cssxsh.mirai.auth

import kotlinx.coroutines.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.event.*
import xyz.cssxsh.mirai.admin.*

public object MiraiAuthenticatorPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.plugin.mirai-authenticator",
        name = "mirai-authenticator",
        version = "1.0.0",
    ) {
        author("cssxsh")

        dependsOn("xyz.cssxsh.mirai.plugin.mirai-administrator", true)
    }
) {
    override fun onEnable() {
        try {
            MiraiAdministrator
        } catch (_: Throwable) {
            MiraiAuthenticator.registerTo(globalEventChannel())
        }
    }

    override fun onDisable() {
        MiraiAuthenticator.cancel()
    }
}