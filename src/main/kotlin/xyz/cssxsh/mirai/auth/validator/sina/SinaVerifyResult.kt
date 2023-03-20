package xyz.cssxsh.mirai.auth.validator.sina

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@PublishedApi
@Serializable
internal data class SinaVerifyResult(
    @SerialName("errno")
    val errno: Int = 0,
    @SerialName("code")
    val code: Int = 0,
    @SerialName("data")
    val `data`: JsonElement = JsonNull,
    @SerialName("msg")
    val message: String = "",
    @SerialName("result")
    val result: Boolean = false
)