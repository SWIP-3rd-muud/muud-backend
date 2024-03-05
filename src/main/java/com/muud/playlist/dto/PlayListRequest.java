package com.muud.playlist.dto;

import com.muud.emotion.entity.Emotion;
import lombok.Data;

import java.util.List;

@Data
public class PlayListRequest {
    private Long id;
    private String title;
    private String videoId;
    private String channelName;
    private List<String> tags;
    private String description;
    private Emotion emotion;
}
