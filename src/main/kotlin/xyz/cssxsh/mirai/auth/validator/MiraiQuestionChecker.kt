package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.event.events.*
import javax.script.*
import kotlin.io.path.*

public class MiraiQuestionChecker : MiraiChecker {
    private val regex = """(?:问题|答案)：(.+)""".toRegex()
    override suspend fun check(event: MemberJoinRequestEvent): Boolean {
        val match = regex.find(event.message) ?: return true
        val question = match.groupValues[1]
        val answer = match.next()?.groupValues?.get(1) ?: return false

        val folder = Path(System.getProperty("xyz.cssxsh.mirai.auth.validator.question", "question"))
        val script = folder.listDirectoryEntries().firstOrNull {
            it.name.startsWith("${event.groupId}.")
        } ?: throw IllegalStateException("获取 ${event.groupId} Question 验证脚本失败")

        val manager = ScriptEngineManager(MiraiProfileChecker::class.java.classLoader)
        val engine = manager.getEngineByExtension(script.extension)
            ?: throw IllegalStateException("获取 ${script.extension} 脚本引擎失败")

        val bindings = engine.createBindings()
        bindings["bot"] = event.bot
        bindings["eventId"] = event.eventId
        bindings["fromId"] = event.fromId
        bindings["fromNick"] = event.fromNick
        bindings["groupId"] = event.groupId
        bindings["groupName"] = event.groupName
        bindings["message"] = event.message
        bindings["invitorId"] = event.invitorId
        bindings["question"] = question
        bindings["answer"] = answer

        val result = try {
            (engine.eval(script.readText(), bindings) as org.luaj.vm2.LuaValue)
                .toboolean()
        } catch (cause: Exception) {
            throw IllegalStateException("验证 ${event.eventId} 失败", cause)
        }

        return result
    }
}