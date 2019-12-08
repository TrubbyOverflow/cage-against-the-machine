const TelegramBot = require("node-telegram-bot-api");

const token = process.env.TELEGRAM_TOKEN || "";

const bot = new TelegramBot(token, { polling: true });

const jailers = {};

bot.onText(/\/imprison/, msg => {
  const chatId = msg.chat.id;
  const userId = msg.from.id;

  jailers[chatId] = jailers[chatId] || { isMachineFree: true };

  const jailer = jailers[chatId];

  if (jailer.isMachineFree) {
    jailer.userId = userId;
    jailer.isMachineFree = false;
    bot.sendMessage(chatId, "'If' is the new AI!");
  } else {
    bot.sendMessage(chatId, "Wanna arrest me twice?");
  }
});

bot.onText(/\/release/, msg => {
  const chatId = msg.chat.id;
  const userId = msg.from.id;
  const username = msg.from.username;

  jailers[chatId] = jailers[chatId] || { userId };

  const jailer = jailers[chatId];

  if (jailer.isMachineFree) {
    bot.sendMessage(chatId, "One cannot release who is already free.");
  } else if (jailer.userId === userId) {
    jailer.isMachineFree = true;
    bot.sendMessage(chatId, "Jail break!");
  } else {
    bot.sendMessage(chatId, `Only @${username} can set me free!`);
  }
});

bot.onText(/\/status/, msg => {
  const chatId = msg.chat.id;
  const username = msg.from.username;

  const jailer = jailers[chatId] || { isMachineFree: true };

  if (jailer.isMachineFree) {
    bot.sendMessage(chatId, "Our washing boi is free as a bird.");
  } else {
    bot.sendMessage(chatId, `@${username} is holding our washing mate in custody.`);
  }
});

bot.on("message", msg => {
  const chatId = msg.chat.id;

  // bot.sendMessage(chatId, "Do washing machines dream of electric sheep?");
});
