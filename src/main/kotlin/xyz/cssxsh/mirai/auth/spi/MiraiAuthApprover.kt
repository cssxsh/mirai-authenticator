package xyz.cssxsh.mirai.auth.spi

import net.mamoe.mirai.event.events.*
import xyz.cssxsh.mirai.auth.*
import xyz.cssxsh.mirai.spi.*

/**
 * SPI 接口
 * @see ComparableService
 */
public class MiraiAuthApprover : MemberApprover {
    // FriendApprover, GroupApprover,
    override val id: String = "mirai-authenticator"
    override val level: Int = 10

//    override suspend fun approve(event: NewFriendRequestEvent): ApproveResult = ApproveResult.Ignore
//
//    override suspend fun approve(event: BotInvitedJoinGroupRequestEvent): ApproveResult = ApproveResult.Ignore

    override suspend fun approve(event: MemberJoinRequestEvent): ApproveResult {
        return when (MiraiAuthenticator.auth(event)) {
            MiraiAuthStatus.PASS -> ApproveResult.Accept
            MiraiAuthStatus.FAIL -> ApproveResult.Reject(black = false, message = "验证失败")
            MiraiAuthStatus.BLACK -> ApproveResult.Reject(black = true, message = "验证失败")
            MiraiAuthStatus.IGNORE -> ApproveResult.Ignore
        }
    }

//    override suspend fun approve(event: FriendAddEvent): ApproveResult = ApproveResult.Ignore
//
//    override suspend fun approve(event: BotJoinGroupEvent): ApproveResult = ApproveResult.Ignore

    override suspend fun approve(event: MemberJoinEvent): ApproveResult {
        return when (MiraiAuthenticator.auth(event)) {
            MiraiAuthStatus.PASS -> ApproveResult.Accept
            MiraiAuthStatus.FAIL -> ApproveResult.Reject(black = false, message = "验证失败")
            MiraiAuthStatus.BLACK -> ApproveResult.Reject(black = true, message = "验证失败")
            MiraiAuthStatus.IGNORE -> ApproveResult.Ignore
        }
    }
}