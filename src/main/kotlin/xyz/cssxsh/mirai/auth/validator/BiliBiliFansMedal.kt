package xyz.cssxsh.mirai.auth.validator

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class BiliBiliFansMedal(
    @SerialName("medal")
    val medal: JsonElement = JsonNull,
    @SerialName("show")
    val show: Boolean = false,
    @SerialName("wear")
    val wear: Boolean = false
)