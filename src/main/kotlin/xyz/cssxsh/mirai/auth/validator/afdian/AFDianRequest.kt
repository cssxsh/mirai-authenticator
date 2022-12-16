package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*

@PublishedApi
@Serializable
internal data class AFDianRequest(
    @SerialName("params")
    val params: String = "",
    @SerialName("sign")
    val sign: String = "",
    @SerialName("ts")
    val timestamp: Int = 0,
    @SerialName("user_id")
    val userId: String = ""
)