package com.example.SpringBot.weather;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApiClient {
  private OkHttpClient okHttpClient;
  private Gson gson;

  public ApiClient() {
    this.okHttpClient = new OkHttpClient();
    this.gson = new Gson();
  }

  public String get(String url) throws Exception {
    Request request = new Request.Builder().url(url).build();
    Response response = okHttpClient.newCall(request).execute();
    String responseBody = response.body().string();
    JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

    // Access the correct JSON elements based on the structure:
    JsonObject mainObject = jsonObject.getAsJsonObject("main");
    String temperature = mainObject.getAsJsonPrimitive("temp").getAsString();
    JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
    String description = weatherArray.get(0).getAsJsonObject().getAsJsonPrimitive("description").getAsString();

    log.info(description);
    String par = temperature + " " + description;
    return par;
  }
}

