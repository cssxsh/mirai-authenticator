package xyz.cssxsh.mirai.auth.validator.bilibili

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@PublishedApi
@Serializable
internal data class BiliBiliFansMedal(
    @SerialName("medal")
    val medal: JsonElement = JsonNull,
    @SerialName("show")
    val show: Boolean = false,
    @SerialName("wear")
    val wear: Boolean = false
)