package xyz.cssxsh.mirai.auth.validator

import kotlinx.serialization.*

@Serializable
internal data class BiliBiliFansMedalDetail(
    @SerialName("day_limit")
    val dayLimit: Int = 0,
    @SerialName("guard_level")
    val guardLevel: Int = 0,
    @SerialName("intimacy")
    val intimacy: Int = 0,
    @SerialName("is_lighted")
    val isLighted: Int = 0,
    @SerialName("level")
    val level: Int = 0,
    @SerialName("light_status")
    val lightStatus: Int = 0,
    @SerialName("medal_color")
    val medalColor: Long = 0,
    @SerialName("medal_color_border")
    val medalColorBorder: Long = 0,
    @SerialName("medal_color_end")
    val medalColorEnd: Long = 0,
    @SerialName("medal_color_start")
    val medalColorStart: Long = 0,
    @SerialName("medal_id")
    val medalId: Long = 0,
    @SerialName("medal_name")
    val medalName: String = "",
    @SerialName("next_intimacy")
    val nextIntimacy: Long = 0,
    @SerialName("score")
    val score: Long = 0,
    @SerialName("target_id")
    val targetId: Long = 0,
    @SerialName("uid")
    val uid: Long = 0,
    @SerialName("wearing_status")
    val wearingStatus: Int = 0
)