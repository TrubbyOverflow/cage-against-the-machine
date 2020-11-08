package dev.knonm.catm

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.Update

data class Jailer(
        var userId: Long? = null,
        var username: String? = null,
        var isMachineFree: Boolean = false
)

fun main() {
    val bot = bot {
        token = System.getenv("TELEGRAM_TOKEN") ?: ""

        val jailersByChatId = mutableMapOf<Long, Jailer?>()

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
                            username = username,
                            isMachineFree = false
                    )
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
