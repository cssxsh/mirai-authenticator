package xyz.cssxsh.mirai.auth.command

import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.contact.*
import xyz.cssxsh.mirai.auth.*
import xyz.cssxsh.mirai.auth.data.*
import xyz.cssxsh.mirai.auth.validator.*
import kotlin.io.path.*

@PublishedApi
internal object MiraiAuthJoinCommand : CompositeCommand(
    owner = MiraiAuthenticatorPlugin,
    primaryName = "auth-join",
    description = "加群请求验证配置"
) {

    @SubCommand
    @Description("进群前检查配置")
    suspend fun CommandSender.check(group: Long, vararg types: String) {
        if (types.any { it !in MiraiChecker.providers }) {
            sendMessage("当前支持的类型 ${MiraiChecker.providers.keys}")
            return
        }
        for (type in types) {
            val script = Path(System.getProperty("xyz.cssxsh.mirai.auth.validator.$type", type), "${group}.lua")
            if (script.isReadable()) continue
            when (type) {
                "profile" -> script.writeText("""return fromProfile:getQLevel() > 4;""")
                "question" -> script.writeText("""return answer == "114514";""")
                "bilibili" -> script.writeText("""return medal:getTargetId() == 11153765 and medal:getLevel() >= 20;""")
            }
            sendMessage("之后，请编辑确认 $script")
        }

        MiraiAuthJoinConfig.checkers[group] = types.asList()
        sendMessage("群 $group 当前检查设置: ${types.joinToString()}")
    }

    @SubCommand
    @Description("进群后验证配置")
    suspend fun CommandSender.validator(group: Long, vararg types: String) {
        if (types.all { it in MiraiValidator.providers }) {
            MiraiAuthJoinConfig.validators[group] = types.asList()
            sendMessage("群 $group 当前验证设置: ${types.joinToString()}")
        } else {
            sendMessage("当前支持的类型 ${MiraiValidator.providers.keys}")
        }
    }

    @SubCommand
    @Description("设置自动放行的QQ号")
    suspend fun CommandSender.official(id: Long) {
        MiraiAuthJoinConfig.official.add(id)

        sendMessage("当前自动放行 ${MiraiAuthJoinConfig.official}")
    }

    @SubCommand
    @Description("问题回答等待时间")
    suspend fun CommandSender.timeout(mills: Long) {
        if (mills < 1_000) {
            sendMessage("单位是 毫秒 ！")
            return
        }
        MiraiAuthJoinConfig.timeout = mills
        sendMessage("目前 等待时间 ${mills}ms")
    }

    @SubCommand
    @Description("问题允许回答次数")
    suspend fun CommandSender.count(value: Int) {
        if (value < 0) {
            sendMessage("至少 1 次")
            return
        }
        MiraiAuthJoinConfig.count = value
        sendMessage("目前 回答次数 $value 次")
    }

    @SubCommand
    @Description("验证码的提示")
    suspend fun CommandSender.tip(message: String) {
        MiraiAuthJoinConfig.tip = message
        sendMessage("目前 验证码的提示 $message")
    }

    @SubCommand
    @Description("加群请求失败交由管理员处理")
    suspend fun CommandSender.place(group: Long) {
        if (MiraiAuthJoinConfig.place.add(group)) {
            sendMessage("加群请求将失败交由管理员处理")
        } else {
            MiraiAuthJoinConfig.place.remove(group)
            sendMessage("取消加群请求失败交由管理员处理")
        }
    }
}