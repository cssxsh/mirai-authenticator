package xyz.cssxsh.mirai.auth.validator.afdian

import kotlinx.serialization.*

@PublishedApi
@Serializable
internal data class AFDianOrder(
    @SerialName("address_address")
    val address: String = "",
    @SerialName("address_person")
    val person: String = "",
    @SerialName("address_phone")
    val phone: String = "",
    @SerialName("discount")
    val discount: String = "",
    @SerialName("month")
    val month: Int = 0,
    @SerialName("out_trade_no")
    val trade: String = "",
    @SerialName("plan_id")
    val planId: String = "",
    @SerialName("plan_title")
    val planTitle: String = "",
    @SerialName("product_type")
    val productType: Int = 0,
    @SerialName("redeem_id")
    val redeemId: String = "",
    @SerialName("remark")
    val remark: String = "",
    @SerialName("show_amount")
    val showAmount: String = "",
    @SerialName("sku_detail")
    val skuDetail: List<SkuDetail> = emptyList(),
    @SerialName("status")
    val status: Int = 0,
    @SerialName("total_amount")
    val totalAmount: String = "",
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("user_private_id")
    val userPrivateId: String = ""
) {
    @Serializable
    data class SkuDetail(
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
        @SerialName("sku_id")
        val skuId: String = "",
        @SerialName("stock")
        val stock: String = ""
    )
}