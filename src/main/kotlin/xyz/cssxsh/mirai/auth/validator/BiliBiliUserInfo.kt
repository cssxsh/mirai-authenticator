package xyz.cssxsh.mirai.auth.validator

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class BiliBiliUserInfo(
    @SerialName("birthday")
    val birthday: String = "",
    @SerialName("coins")
    val coins: Int = 0,
    @SerialName("elec")
    val elec: JsonElement = JsonNull,
    @SerialName("face")
    val face: String = "",
    @SerialName("face_nft")
    val faceNft: Int = 0,
    @SerialName("face_nft_type")
    val faceNftType: Int = 0,
    @SerialName("fans_badge")
    val fansBadge: Boolean = false,
    @SerialName("fans_medal")
    val fansMedal: JsonElement = JsonNull,
    @SerialName("gaia_data")
    val gaiaData: JsonElement = JsonNull,
    @SerialName("gaia_res_type")
    val gaiaResType: Int = 0,
    @SerialName("is_followed")
    val isFollowed: Boolean = false,
    @SerialName("is_risk")
    val isRisk: Boolean = false,
    @SerialName("is_senior_member")
    val isSeniorMember: Int = 0,
    @SerialName("jointime")
    val jointime: Int = 0,
    @SerialName("level")
    val level: Int = 0,
    @SerialName("live_room")
    val liveRoom: JsonElement = JsonNull,
    @SerialName("mcn_info")
    val mcnInfo: JsonElement = JsonNull,
    @SerialName("mid")
    val mid: Int = 0,
    @SerialName("moral")
    val moral: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("nameplate")
    val nameplate: JsonElement = JsonNull,
    @SerialName("official")
    val official: JsonElement = JsonNull,
    @SerialName("pendant")
    val pendant: JsonElement = JsonNull,
    @SerialName("profession")
    val profession: JsonElement = JsonNull,
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("school")
    val school: JsonElement = JsonNull,
    @SerialName("series")
    val series: JsonElement = JsonNull,
    @SerialName("sex")
    val sex: String = "",
    @SerialName("sign")
    val sign: String = "",
    @SerialName("silence")
    val silence: Int = 0,
    @SerialName("sys_notice")
    val sysNotice: JsonElement = JsonNull,
    @SerialName("tags")
    val tags: JsonElement = JsonNull,
    @SerialName("theme")
    val theme: JsonElement = JsonNull,
    @SerialName("top_photo")
    val topPhoto: String = "",
    @SerialName("user_honour_info")
    val userHonourInfo: JsonElement = JsonNull,
    @SerialName("vip")
    val vip: JsonElement = JsonNull,
)