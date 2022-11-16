package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import javax.script.*
import kotlin.io.path.*

/**
 * 校验加群问题
 */
@PublishedApi
internal class MiraiQuestionChecker : MiraiChecker {
    override val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)

    override suspend fun check(event: MemberJoinRequestEvent): Boolean {
        val match = MiraiChecker.QA.find(event.message) ?: return true
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