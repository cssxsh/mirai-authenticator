package xyz.cssxsh.mirai.auth.validator

import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.utils.*
import java.nio.file.*
import javax.script.*

/**
 * 校验接口
 */
public abstract class AbstractMiraiChecker : MiraiChecker {
    protected open val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)
    protected val manager: ScriptEngineManager = ScriptEngineManager(this::class.java.classLoader)
    protected abstract val folder: Path

    /**
     * 装入基本 Bindings
     */
    public override fun <T : Bindings> T.apply(event: MemberJoinRequestEvent): T = apply {
        this["bot"] = event.bot
        this["logger"] = logger
        this["eventId"] = event.eventId
        this["fromId"] = event.fromId
        this["fromNick"] = event.fromNick
        this["groupId"] = event.groupId
        this["groupName"] = event.groupName
        this["message"] = event.message
        this["invitorId"] = event.invitorId
    }
}