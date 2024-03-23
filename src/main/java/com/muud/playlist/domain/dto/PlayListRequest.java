package com.muud.playlist.domain.dto;

import com.muud.emotion.domain.Emotion;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PlayListRequest {
    Map<Emotion, List<String>> playLists;
}
