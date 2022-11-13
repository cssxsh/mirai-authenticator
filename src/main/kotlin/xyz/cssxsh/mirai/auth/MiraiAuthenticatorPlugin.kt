package xyz.cssxsh.mirai.auth

import kotlinx.coroutines.*
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.event.*
import xyz.cssxsh.mirai.admin.*
import xyz.cssxsh.mirai.auth.command.*
import xyz.cssxsh.mirai.auth.data.*
import xyz.cssxsh.mirai.auth.validator.*
import javax.script.*

@PublishedApi
internal object MiraiAuthenticatorPlugin : KotlinPlugin(
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
        MiraiAuthJoinConfig.reload()
        try {
            MiraiAdministrator
        } catch (_: NoClassDefFoundError) {
            MiraiAuthenticator.registerTo(globalEventChannel())
        }
        val lua = ScriptEngineManager(jvmPluginClasspath.pluginClassLoader).getEngineByName("lua")
        if (lua == null) {
            jvmPluginClasspath.downloadAndAddToPath(
                jvmPluginClasspath.pluginIndependentLibrariesClassLoader,
                listOf("org.luaj:luaj-jse:3.0.1")
            )
        }

        for ((key, _) in MiraiChecker.providers) {
            val profile = resolveDataFile(key)
            profile.mkdirs()
            System.setProperty("xyz.cssxsh.mirai.auth.validator.${key}", profile.path)
        }

        MiraiAuthJoinCommand.register()
    }

    override fun onDisable() {
        MiraiAuthenticator.cancel()
        MiraiAuthJoinCommand.unregister()
    }
}