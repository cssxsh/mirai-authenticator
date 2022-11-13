package xyz.cssxsh.mirai.auth.validator

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.json.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import xyz.cssxsh.mirai.auth.data.*

/**
 * 验证器，验证码实现
 */
public class MiraiCaptchaValidator : MiraiValidator {
    private val http: HttpClient = HttpClient(OkHttp) {
        BrowserUserAgent()
        ContentEncoding()
        Charsets {
            responseCharsetFallback = Charset.forName("GBK")
        }
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
    }

    internal suspend fun getCaptchaImage(): ByteArray {
        val statement = http.prepareGet("https://mail.sina.com.cn/cgi-bin/imgcode.php") {
            header(HttpHeaders.Referrer, "https://mail.sina.com.cn/register/regmail.php")
            header(HttpHeaders.Origin, "https://mail.sina.com.cn")
        }
        return statement.execute { response ->
            if (response.contentType() != ContentType.Image.JPEG) {
                throw ResponseException(response, "")
            }
            response.body()
        }
    }

    internal suspend fun verifyCaptcha(code: String): SinaVerifyResult {
        val statement = http.prepareForm("https://mail.sina.com.cn/cgi-bin/RegPhoneCode.php", Parameters.build {
            append("phonenumber", "15874523695")
            append("email", "fgsj842376tysd@sina.com")
            append("imgvcode", code)
        }) {
            header(HttpHeaders.Referrer, "https://mail.sina.com.cn/register/regmail.php")
            header(HttpHeaders.Origin, "https://mail.sina.com.cn")
        }
        return statement.execute { response ->
            val text = response.bodyAsText()
            if (response.status != HttpStatusCode.OK) {
                throw ResponseException(response, text)
            }
            try {
                Json.decodeFromString(SinaVerifyResult.serializer(), text)
            } catch (cause: Exception) {
                throw ResponseException(response, text)
                    .initCause(cause)
            }
        }
    }

    override suspend fun question(event: MemberJoinEvent): Message {
        val bytes = getCaptchaImage()
        return bytes.toExternalResource().use { event.group.uploadImage(it) } + MiraiAuthJoinConfig.tip
    }

    override suspend fun auth(answer: String): Boolean {
        return when(verifyCaptcha(code = answer).code) {
            -102 -> false
            else -> true
        }
    }
}