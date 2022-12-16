package xyz.cssxsh.mirai.auth.validator.afdian

public class AFDianApiException(public val body: AFDianDataWrapper) : IllegalStateException() {
    override val message: String = "Error Code: ${body.code} Error Message: ${body.message} Data: ${body.data}"
}