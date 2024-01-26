package com.example.SpringBot.bot;

import com.example.SpringBot.bot.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
  final BotConfig config;

  public TelegramBot(BotConfig config) {
    this.config = config;
  }

  @Override
  public String getBotUsername() {
    return config.getBotName();
  }

  @Override
  public String getBotToken() {
    return config.getToken();
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String messageText = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      switch (messageText) {
        case "/start":
          startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
          break;
        default:
          sendMessage(chatId, "Sorry i do not know this phrase");
      }
    }
  }

  private void startCommandReceived(long chatId, String name) {
    String answer = EmojiParser.parseToUnicode("hi" + name);
    log.info("Replied to user " + name);
    sendMessage(chatId, answer);
  }

  private void sendMessage(long chatId, String textToSend) {
    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setText(textToSend);
    try {
      execute(message);
    } catch (TelegramApiException e) {

    }
  }
}