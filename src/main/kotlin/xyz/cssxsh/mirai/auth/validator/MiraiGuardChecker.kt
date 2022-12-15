package xyz.cssxsh.mirai.auth.validator

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.event.events.*
import xyz.cssxsh.mirai.auth.validator.bilibili.*
import kotlin.io.path.*

/**
 * 校验 b站粉丝牌
 */
@PublishedApi
internal class MiraiGuardChecker : MiraiChecker, AbstractMiraiChecker() {
    override val folder = Path(System.getProperty("xyz.cssxsh.mirai.auth.validator.bilibili", "bilibili"))
    private val http: HttpClient = HttpClient(OkHttp) {
        BrowserUserAgent()
        ContentEncoding()
    }

    override suspend fun check(event: MemberJoinRequestEvent): Boolean {
        val script = folder.listDirectoryEntries().firstOrNull { it.name.startsWith("${event.groupId}.") }
            ?: throw IllegalStateException("获取 ${event.groupId} Guard 验证脚本失败")
        val engine = manager.getEngineByExtension(script.extension)
            ?: throw NoSuchElementException("获取 ${script.extension} 脚本引擎失败")

        val match = MiraiChecker.QA.find(event.message) ?: return true
        val question = match.groupValues[1]
        val answer = match.next()?.groupValues?.get(1) ?: return false
        val mid = answer.trim().toLong()

        val medal = try {
            fetchFansMedal(mid = mid)
        } catch (cause: Exception) {
            throw IllegalStateException("查询 ${event.fromId}-${mid} 信息失败", cause)
        }

        val bindings = engine.createBindings().apply(event = event)
        bindings["question"] = question
        bindings["answer"] = answer
        bindings["mid"] = mid
        bindings["medal"] = medal

        val result = try {
            (engine.eval(script.readText(), bindings) as org.luaj.vm2.LuaValue)
                .toboolean()
        } catch (cause: Exception) {
            throw IllegalStateException("验证 ${event.eventId} 失败", cause)
        }

        return result
    }

    @PublishedApi
    internal suspend fun fetchFansMedal(mid: Long): BiliBiliFansMedalDetail {
        val statement = http.prepareGet("https://api.bilibili.com/x/space/acc/info") {
            parameter("mid", mid)
            parameter("platform", "web")
            parameter("jsonp", "jsonp")
        }

        return statement.execute { response ->
            val text = response.body<String>()
            if (response.status != HttpStatusCode.OK) throw ResponseException(response, text)
            try {
                val result = Json.decodeFromString(BiliBiliResult.serializer(), text)
                val info = Json.decodeFromJsonElement(BiliBiliUserInfo.serializer(), result.data)
                val medal = Json.decodeFromJsonElement(BiliBiliFansMedal.serializer(), info.fansMedal)
                Json.decodeFromJsonElement(BiliBiliFansMedalDetail.serializer(), medal.medal)
            } catch (cause: Exception) {
                throw ResponseException(response, text)
                    .initCause(cause)
            }
        }
    }
}