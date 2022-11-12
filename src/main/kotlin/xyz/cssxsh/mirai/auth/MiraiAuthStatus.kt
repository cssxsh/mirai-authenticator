package xyz.cssxsh.mirai.auth

/**
 * 认证状态
 * @property PASS 通过
 * @property FAIL 失败
 * @property BLACK 拉黑
 * @property IGNORE 忽略
 */
public enum class MiraiAuthStatus {
    PASS, FAIL, BLACK, IGNORE
}