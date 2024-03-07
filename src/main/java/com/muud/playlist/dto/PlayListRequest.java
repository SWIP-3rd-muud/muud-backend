package com.muud.playlist.dto;

import com.muud.emotion.entity.Emotion;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PlayListRequest {
    Map<Emotion, List<String>> playLists;
}
