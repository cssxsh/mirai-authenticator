package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
internal data class AFDianSponsorPlan(
    @SerialName("bundle_sku_select_count")
    val bundleSkuSelectCount: Int = 0,
    @SerialName("bundle_stock")
    val bundleStock: Int = 0,
    @SerialName("can_buy_hide")
    val canBuyHide: Int = 0,
    @SerialName("can_ali_agreement")
    val canAliAgreement: Int = 0,
    @SerialName("config")
    val config: Config = Config(),
    @SerialName("coupon")
    val coupon: List<JsonElement> = emptyList(),
    @SerialName("desc")
    val description: String = "",
    @SerialName("expire_time")
    val expireTime: Int = 0,
    @SerialName("favorable_price")
    val favorablePrice: Int = 0,
    @SerialName("has_coupon")
    val hasCoupon: Int = 0,
    @SerialName("has_plan_config")
    val hasPlanConfig: Int = 0,
    @SerialName("independent")
    val independent: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("need_address")
    val needAddress: Int = 0,
    @SerialName("need_invite_code")
    val needInviteCode: Boolean = false,
    @SerialName("pay_month")
    val payMonth: Int = 0,
    @SerialName("permanent")
    val permanent: Int = 0,
    @SerialName("pic")
    val picture: String = "",
    @SerialName("plan_id")
    val planId: String = "",
    @SerialName("price")
    val price: String = "",
    @SerialName("product_type")
    val productType: Int = 0,
    @SerialName("rank")
    val rank: Int = 0,
    @SerialName("rankType")
    val rankType: Int = 0,
    @SerialName("sale_limit_count")
    val saleLimitCount: Int = 0,
    @SerialName("shipping_fee_info")
    val shippingFeeInfo: JsonElement = JsonNull,
    @SerialName("show_price")
    val showPrice: String = "",
    @SerialName("show_price_after_adjust")
    val showPriceAfterAdjust: String = "",
    @SerialName("sku_processed")
    val skuProcessed: List<SkuProcessed> = emptyList(),
    @SerialName("status")
    val status: Int = 0,
    @SerialName("timing")
    val timing: Timing = Timing(),
    @SerialName("update_time")
    val updateTime: Int = 0,
    @SerialName("user_id")
    val userId: String = ""
) {
    @Serializable
    data class Config(
        @SerialName("create_time")
        val createTime: Int = 0,
        @SerialName("id")
        val id: Int = 0,
        @SerialName("plan_id")
        val planId: String = "",
        @SerialName("remark_name")
        val remarkName: String = "",
        @SerialName("remark_placeholder")
        val remarkPlaceholder: String = "",
        @SerialName("remark_required")
        val remarkRequired: Int = 0,
        @SerialName("status")
        val status: Int = 0,
        @SerialName("update_time")
        val updateTime: Int = 0,
        @SerialName("user_id")
        val userId: String = ""
    )

    @Serializable
    data class SkuProcessed(
        @SerialName("album_id")
        val albumId: String = "",
        @SerialName("count")
        val count: Int = 0,
        @SerialName("name")
        val name: String = "",
        @SerialName("pic")
        val picture: String = "",
        @SerialName("post_id")
        val postId: String = "",
        @SerialName("price")
        val price: Double = 0.0,
        @SerialName("sku_id")
        val skuId: String = "",
        @SerialName("stock")
        val stock: String = ""
    )

    @Serializable
    data class Timing(
        @SerialName("timing_off")
        val timingOff: Int = 0,
        @SerialName("timing_on")
        val timingOn: Int = 0,
        @SerialName("timing_sell_off")
        val timingSellOff: Int = 0,
        @SerialName("timing_sell_on")
        val timingSellOn: Int = 0
    )
}