package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
public data class AFDianDataWrapper(
    @SerialName("data")
    val data: JsonElement = JsonNull,
    @SerialName("ec")
    val code: Int = 0,
    @SerialName("em")
    val message: String = ""
)