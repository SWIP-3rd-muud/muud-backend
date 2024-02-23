package com.muud.emotion.service;

import com.muud.emotion.entity.Emotion;
import com.muud.emotion.dto.EmotionResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmotionService {

    public EmotionResponse getEmotionResponse(String emotionName) {
        return Arrays.stream(Emotion.values()).filter(emotion -> emotion.name().equalsIgnoreCase(emotionName))
                .findFirst()
                .map(EmotionResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + emotionName));
    }

    public List<EmotionResponse> getEmotionResponseList() {
        return Arrays.stream(Emotion.values())
                .map(EmotionResponse::from)
                .collect(Collectors.toList());
    }
}
