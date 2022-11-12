package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.*
import net.mamoe.mirai.event.events.*
import javax.script.*
import kotlin.io.path.*

public class MiraiProfileChecker : MiraiChecker {
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

        val bindings = engine.createBindings()
        bindings["bot"] = event.bot
        bindings["event_id"] = event.eventId
        bindings["from_id"] = event.fromId
        bindings["from_nick"] = event.fromNick
        bindings["from_profile"] = profile
        bindings["group_id"] = event.groupId
        bindings["group_name"] = event.groupName
        bindings["message"] = event.message
        bindings["invitor_id"] = event.invitorId

        val result = try {
            (engine.eval(script.readText(), bindings) as org.luaj.vm2.LuaValue)
                .toboolean()
        } catch (cause: Exception) {
            throw IllegalStateException("验证 ${event.eventId} 失败", cause)
        }

        return result
    }
}