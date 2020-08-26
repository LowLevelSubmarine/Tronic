package com.tronic.bot.smartbot;

import com.tronic.bot.core.Tronic;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import java.io.IOException;
import java.net.URL;


public class SmartBot {
    Logger logger = LogManager.getLogger(Tronic.class);
    private final static String API_PREFIX = "https://www.google.com/complete/search?q=";
    private final static String API_SUFFIX = "&client=chrome-omni&gs_ri=chrome-ext-ansg";
    public SmartBot (MessageReceivedEvent event) {
        String rawMessage = event.getMessage().getContentRaw();
        String message = rawMessage.replaceAll("<(.*?)> ","");
        if (message.split(" ").length > 1) {
            searchApi(message,event);
        } else {
            searchApi(message,event);
        }
    }

    public void searchApi(String content,MessageReceivedEvent event) {
        try {
            String getRequest = getServerResponse(new URL((API_PREFIX + content + API_SUFFIX).replace(" ","+")));
            JSONArray json = new JSONArray(getRequest);
            String answer = json.getJSONObject(4).getJSONArray("google:suggestdetail").getJSONObject(0).getJSONObject("ansa").getJSONArray("l").getJSONObject(1).getJSONObject("il").getJSONArray("t").getJSONObject(0).getString("t");
            event.getChannel().sendMessage(answer).mention(event.getAuthor()).queue();
        } catch ( IOException e) {
            logger.error("Some weird URL issues on api request");
        }

    }

    private String getServerResponse(URL url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.code()!=200) throw new IOException();
        return response.body().string();
    }

}
