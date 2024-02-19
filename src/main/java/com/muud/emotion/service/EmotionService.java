package com.muud.emotion.service;

import com.muud.emotion.entity.Emotion;
import com.muud.emotion.dto.EmotionResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EmotionService {

    public EmotionResponse getEmotionResponse(String emotionEnum) {
        return Arrays.stream(Emotion.values()).filter(emotion -> emotion.name().equalsIgnoreCase(emotionEnum))
                .findFirst()
                .map(EmotionResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + emotionEnum));
    }
}
