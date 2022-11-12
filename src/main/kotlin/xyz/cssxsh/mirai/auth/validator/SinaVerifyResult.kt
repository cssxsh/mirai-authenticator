package xyz.cssxsh.mirai.auth.validator

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class SinaVerifyResult(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("data")
    val `data`: JsonElement = JsonNull,
    @SerialName("msg")
    val message: String = "",
    @SerialName("result")
    val result: Boolean = false
)