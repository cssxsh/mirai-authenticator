package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*

@PublishedApi
@Serializable
internal data class AFDianSponsor(
    @SerialName("all_sum_amount")
    val allSumAmount: String = "",
    @SerialName("current_plan")
    val currentPlan: AFDianSponsorPlan = AFDianSponsorPlan(),
    @SerialName("first_pay_time")
    val firstPayTime: Int = 0,
    @SerialName("last_pay_time")
    val lastPayTime: Int = 0,
    @SerialName("sponsor_plans")
    val sponsorPlans: List<AFDianSponsorPlan> = emptyList(),
    @SerialName("user")
    val user: AFDianUser = AFDianUser()
)