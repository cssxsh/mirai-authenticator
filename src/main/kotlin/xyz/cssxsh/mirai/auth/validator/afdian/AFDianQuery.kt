package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*

@PublishedApi
@Serializable
internal data class AFDianQuery<T>(
    @SerialName("list")
    val list: List<T> = emptyList(),
    @SerialName("request")
    val request: AFDianRequest = AFDianRequest(),
    @SerialName("total_count")
    val count: Int = 0,
    @SerialName("total_page")
    val page: Int = 0
)