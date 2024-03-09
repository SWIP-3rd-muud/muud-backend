package com.muud.emotion.service;

import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.dto.EmotionCountResponse;
import com.muud.emotion.domain.dto.QuestionResponse;
import com.muud.emotion.domain.Emotion;
import com.muud.emotion.domain.dto.EmotionResponse;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmotionService {

    public EmotionResponse getEmotionResponse(final String emotionName) {
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

    public List<EmotionCountResponse> getEmotionCount(final List<Diary> diaryList,
                                                      final boolean ascending) {
        Map<Emotion, Long> emotionCountMap = new HashMap<>();

        for (Emotion emotionValue : Emotion.values()) {
            long count = diaryList.stream().filter(emotion -> emotion.getEmotion() == emotionValue).count();
            emotionCountMap.put(emotionValue, count);
        }

        Comparator<EmotionCountResponse> comparator = Comparator.comparing(EmotionCountResponse::count);
        if (!ascending) {
            comparator = comparator.reversed();
        }

        return emotionCountMap.entrySet().stream()
                .map(entry -> new EmotionCountResponse(entry.getKey().name(), entry.getValue()))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public List<QuestionResponse> getAllQuestionResponse() {
        return Arrays.stream(Emotion.values())
                .map(question -> new QuestionResponse(question.name(), question.getQuestion()))
                .collect(Collectors.toList());
    }
}
