package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*

@Serializable
internal data class AFDianUser(
    @SerialName("avatar")
    val avatar: String = "",
    @SerialName("name")
    val name: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("user_private_id")
    val userPrivateId: String = ""
)