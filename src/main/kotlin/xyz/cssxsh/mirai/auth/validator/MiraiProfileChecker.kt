package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import javax.script.*
import kotlin.io.path.*

/**
 * 校验简介信息
 */
@PublishedApi
internal class MiraiProfileChecker : MiraiChecker {
    override val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)

    override suspend fun check(event: MemberJoinRequestEvent): Boolean {
        val folder = Path(System.getProperty("xyz.cssxsh.mirai.auth.validator.profile", "profile"))
        val script = folder.listDirectoryEntries().firstOrNull { it.name.startsWith("${event.groupId}.") }
            ?: throw IllegalStateException("获取 ${event.groupId} Profile 验证脚本失败")

        val manager = ScriptEngineManager(MiraiProfileChecker::class.java.classLoader)
        val engine = manager.getEngineByExtension(script.extension)
            ?: throw IllegalStateException("获取 ${script.extension} 脚本引擎失败")

        val profile = try {
            Mirai.queryProfile(event.bot, event.fromId)
        } catch (cause: Exception) {
            throw IllegalStateException("查询 ${event.fromId} 信息失败", cause)
        }

        val bindings = engine.createBindings().apply(event = event)
        bindings["fromProfile"] = profile

        val result = try {
            (engine.eval(script.readText(), bindings) as org.luaj.vm2.LuaValue)
                .toboolean()
        } catch (cause: Exception) {
            throw IllegalStateException("验证 ${event.eventId} 失败", cause)
        }

        return result
    }
}