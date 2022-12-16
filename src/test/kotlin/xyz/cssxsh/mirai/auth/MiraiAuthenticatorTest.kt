package xyz.cssxsh.mirai.auth

import kotlinx.coroutines.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.mock.*
import org.junit.jupiter.api.*
import xyz.cssxsh.mirai.auth.data.*
import xyz.cssxsh.mirai.auth.validator.*
import java.io.File

internal class MiraiAuthenticatorTest {
    private val bot = MockBotFactory.newMockBotBuilder().create()
    private val group = bot.addGroup(123456, "mock")

    init {
        MiraiAuthenticator.registerTo(GlobalEventChannel)
        for ((key, _) in MiraiChecker.providers) {
            val folder = File("example/${key}")
            folder.mkdirs()
            System.setProperty("xyz.cssxsh.mirai.auth.validator.${key}", folder.path)
        }
    }

    @Test
    fun profile(): Unit = runBlocking {
        MiraiAuthJoinConfig.checkers[123456] = listOf("profile")
        group.broadcastNewMemberJoinRequestEvent(
            requester = 123456789,
            requesterName = "...",
            message = ""
        )
    }

    @Test
    fun question(): Unit = runBlocking {
        MiraiAuthJoinConfig.checkers[123456] = listOf("question")
        group.broadcastNewMemberJoinRequestEvent(
            requester = 123456789,
            requesterName = "...",
            message = """
                问题：114514
                答案：1919810
            """.trimIndent()
        )
    }

    @Test
    fun bilibili(): Unit = runBlocking {
        MiraiAuthJoinConfig.checkers[123456] = listOf("bilibili")
        group.broadcastNewMemberJoinRequestEvent(
            requester = 123456789,
            requesterName = "...",
            message = """
                问题：你的UID
                答案：730732
            """.trimIndent()
        )
    }

    @Test
    fun afdian(): Unit = runBlocking {
        MiraiAuthJoinConfig.checkers[123456] = listOf("afdian")
        group.broadcastNewMemberJoinRequestEvent(
            requester = 123456789,
            requesterName = "...",
            message = """
                问题：你的爱发电ID
                答案：730732
            """.trimIndent()
        )
    }
}