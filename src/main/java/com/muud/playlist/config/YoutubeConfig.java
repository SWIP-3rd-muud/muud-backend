package com.muud.playlist.config;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutubeConfig {
    @Bean(name = "youtube")
    public YouTube youTube() {
        JsonFactory jsonFactory = new JacksonFactory();
        return new YouTube.Builder(new NetHttpTransport(), jsonFactory, request -> {})
                .setApplicationName("Muud")
                .build();
    }

}
