package com.example.SpringBot.bot;

import com.example.SpringBot.bot.config.BotConfig;
import com.example.SpringBot.weather.ApiClient;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
  final BotConfig config;
  private final ApiClient apiClient;

  public TelegramBot(BotConfig config, ApiClient apiClient) {
    this.config = config;
    this.apiClient = apiClient;
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

        case "/Kyiv":
          String value = "Kyiv";
          String url = "https://api.openweathermap.org/data/2.5/weather?q=" + value + ",UA&appid=81e14c41d7181acc9ac8535fdf0a7a91";
          try {
            String temperature;
            String description;
            String par = apiClient.get(url);
            int index = par.indexOf(' ');
            String[] paramerts = new String[]{par.substring(0, index), par.substring(index + 1)};
            temperature = paramerts[0];
            description = paramerts[1];
            double temp = Double.parseDouble(temperature);
            double celsius = (double) (temp - 273.15);

            String message = "Погода в " + value + ":\n\nТемпература: " + Math.round(celsius) + "\nОпис: " + description;

            sendMessage(chatId, message);
          } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Виникла помилка під час отримання погоди в Калуші.");
          }
          break;

        case "/Ivano-Frankivsk":
          value = "Ivano-Frankivsk";
          url = "https://api.openweathermap.org/data/2.5/weather?q=" + value + ",UA&appid=81e14c41d7181acc9ac8535fdf0a7a91";
          try {
            String temperature;
            String description;
            String par = apiClient.get(url);
            int index = par.indexOf(' ');
            String[] paramerts = new String[]{par.substring(0, index), par.substring(index + 1)};
            temperature = paramerts[0];
            description = paramerts[1];
            double temp = Double.parseDouble(temperature);
            double celsius = (double) (temp - 273.15);

            String message = "Погода в " + value + ":\n\nТемпература: " + Math.round(celsius) + "\nОпис: " + description;

            sendMessage(chatId, message);
          } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Виникла помилка під час отримання погоди в Калуші.");
          }
          break;

        default:
          sendMessage(chatId, "Sorry i do not know this phrase");
      }
    }
  }

  private void startCommandReceived(long chatId, String name) {
    String answer = EmojiParser.parseToUnicode("Hi " + name + ", in which city do you want to see the weather?");
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
