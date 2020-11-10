package dev.knonm.catm

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.Update
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Paths

const val JAILERS_PATH = "jailers.json"

@Serializable
data class Jailer(
        val userId: Long? = null,
        val username: String? = null
)

fun writeTextToFile(path: String, content: String) {
    val filePath = Paths.get(path)

    if (Files.notExists(filePath)) {
        Files.createFile(filePath)
    }

    Files.writeString(filePath, content)
}

fun readTextFromFile(path: String): String? {
    val pathObj = Paths.get(path)

    if (Files.notExists(pathObj)) {
        return null
    }

    return Files.readAllLines(pathObj).reduce { acc, s -> acc + s }
}

fun saveJailersByChatId(jailersByChatId: Map<Long, Jailer?>) {
    val content = Json.encodeToString(jailersByChatId)

    writeTextToFile(JAILERS_PATH, content)
}

fun retrieveJailersByChatId(): MutableMap<Long, Jailer?> {
    val content = readTextFromFile(JAILERS_PATH)

    return content?.let {
        Json.decodeFromString(content)
    } ?: mutableMapOf()
}

fun main() {
    val bot = bot {
        token = System.getenv("TELEGRAM_TOKEN") ?: ""

        val jailersByChatId = retrieveJailersByChatId()

        dispatch {

            command("imprison") { bot: Bot, update: Update ->
                val message = update.message!!
                val from = message.from!!

                val chatId = message.chat.id
                val userId = from.id
                val username = from.username

                jailersByChatId[chatId]?.let {
                    bot.sendMessage(chatId, "Wanna arrest me twice?")
                } ?: run {
                    jailersByChatId[chatId] = Jailer(
                            userId = userId,
                            username = username
                    )
                    saveJailersByChatId(jailersByChatId)
                    bot.sendMessage(chatId, "'If' is the new AI!")
                }
            }

            command("release") { bot: Bot, update: Update ->
                val message = update.message!!
                val from = message.from!!

                val chatId = message.chat.id
                val userId = from.id

                jailersByChatId[chatId]?.let {
                    if (it.userId == userId) {
                        jailersByChatId[chatId] = null
                        saveJailersByChatId(jailersByChatId)
                        bot.sendMessage(chatId, "Jail break!")
                    } else {
                        bot.sendMessage(chatId, "Only @${it.username} can set me free!")
                    }
                } ?: run {
                    bot.sendMessage(chatId, "One cannot release who is already free.")
                }
            }

            command("status") { bot: Bot, update: Update ->
                val message = update.message!!

                val chatId = message.chat.id

                jailersByChatId[chatId]?.let {
                    bot.sendMessage(
                            chatId,
                            "@${it.username} is holding our washing mate in custody."
                    )
                } ?: bot.sendMessage(chatId, "Our washing boi is free as a bird.")
            }
        }
    }

    bot.startPolling()
}
