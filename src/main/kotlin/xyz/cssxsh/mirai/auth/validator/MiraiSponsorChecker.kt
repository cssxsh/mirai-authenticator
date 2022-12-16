package xyz.cssxsh.mirai.auth.validator

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.event.events.*
import xyz.cssxsh.mirai.auth.validator.afdian.*
import java.security.*
import kotlin.io.path.*
import kotlin.random.*

/**
 * 校验爱发电
 */
@PublishedApi
internal class MiraiSponsorChecker : MiraiChecker, AbstractMiraiChecker() {
    override val folder = Path(System.getProperty("xyz.cssxsh.mirai.auth.validator.afdian", "afdian"))
    private val http: HttpClient = HttpClient(OkHttp) {
        BrowserUserAgent()
        ContentEncoding()
    }
    private val uid: String = System.getenv("AFDIAN_USER_ID")
        ?: System.getProperty("xyz.cssxsh.mirai.auth.validator.afdian.uid")
        ?: throw NoSuchElementException("AFDIAN_USER_ID")
    private val token: String = System.getenv("AFDIAN_USER_TOKEN")
        ?: System.getProperty("xyz.cssxsh.mirai.auth.validator.afdian.token")
        ?: throw NoSuchElementException("AFDIAN_USER_TOKEN")

    override suspend fun check(event: MemberJoinRequestEvent): Boolean {
        val match = MiraiChecker.QA.find(event.message) ?: return true
        val question = match.groupValues[1]
        val answer = match.next()?.groupValues?.get(1) ?: return false
        val script = folder.listDirectoryEntries().firstOrNull { it.name.startsWith("${event.groupId}.") }
            ?: throw IllegalStateException("获取 ${event.groupId} Sponsor 验证脚本失败")
        val engine = manager.getEngineByExtension(script.extension)
            ?: throw NoSuchElementException("获取 ${script.extension} 脚本引擎失败")

        val bindings = engine.createBindings().apply(event = event)
        bindings["question"] = question
        bindings["answer"] = answer

        return supervisorScope {
            var page = 1
            var total = 0
            while (isActive) {
                val query = sponsor(uid = uid, token = token, page = page++)

                bindings["query"] = query
                val result = try {
                    (engine.eval(script.readText(), bindings) as org.luaj.vm2.LuaValue)
                        .toboolean()
                } catch (cause: Exception) {
                    throw IllegalStateException("验证 ${event.eventId} 失败", cause)
                }
                if (result) return@supervisorScope true

                if (total >= query.count || query.list.isEmpty()) break
            }

            false
        }
    }

    private fun sign(token: String, params: String, timestamp: Long, uid: String): String {
        val digest = MessageDigest.getInstance("md5")
        digest.update(token.toByteArray())
        digest.update("params".toByteArray())
        digest.update(params.toByteArray())
        digest.update("ts".toByteArray())
        digest.update(timestamp.toString().toByteArray())
        digest.update("user_id".toByteArray())
        digest.update(uid.toByteArray())
        val bytes = digest.digest()

        return "%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x".format(args = bytes.toTypedArray())

    }

    private fun HttpRequestBuilder.body(uid: String, token: String, builder: JsonObjectBuilder.() -> Unit) {
        val timestamp = System.currentTimeMillis() / 1_000
        val params = buildJsonObject(builder).toString()
        val sign = sign(token = token, params = params, timestamp = timestamp, uid = uid)
        contentType(ContentType.Application.Json)
        setBody(buildJsonObject {
            put("user_id", uid)
            put("params", params)
            put("ts", timestamp)
            put("sign", sign)
        }.toString())
    }

    @PublishedApi
    internal suspend fun ping(uid: String, token: String): JsonObject {
        val statement = http.preparePost("https://afdian.net/api/open/ping") {
            body(uid, token) {
                put("page", Random.Default.nextInt())
            }
        }

        val json = statement.body<String>()
        val wrapper = Json.decodeFromString<AFDianDataWrapper>(json)
        if (wrapper.code != 200) throw AFDianApiException(body = wrapper)
        return Json.decodeFromJsonElement(wrapper.data)
    }

    @PublishedApi
    internal suspend fun order(uid: String, token: String, page: Int): AFDianQuery<AFDianOrder> {
        val statement = http.preparePost("https://afdian.net/api/open/query-order") {
            body(uid, token) {
                put("page", page)
            }
        }

        val json = statement.body<String>()
        val wrapper = Json.decodeFromString<AFDianDataWrapper>(json)
        if (wrapper.code != 200) throw AFDianApiException(body = wrapper)
        return Json.decodeFromJsonElement(wrapper.data)
    }

    @PublishedApi
    internal suspend fun sponsor(uid: String, token: String, page: Int): AFDianQuery<AFDianSponsor> {
        val statement = http.preparePost("https://afdian.net/api/open/query-sponsor") {
            body(uid, token) {
                put("page", page)
            }
        }

        val json = statement.body<String>()
        val wrapper = Json.decodeFromString<AFDianDataWrapper>(json)
        if (wrapper.code != 200) throw AFDianApiException(body = wrapper)
        return Json.decodeFromJsonElement(wrapper.data)
    }
}