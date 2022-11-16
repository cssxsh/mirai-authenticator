package xyz.cssxsh.mirai.auth.validator

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class BiliBiliResult(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("data")
    val `data`: JsonElement = JsonNull,
    @SerialName("message")
    val message: String = "",
    @SerialName("ttl")
    val ttl: Int = 0
)