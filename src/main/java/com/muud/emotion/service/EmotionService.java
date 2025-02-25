package com.muud.emotion.service;

import com.muud.diary.domain.Diary;
import com.muud.emotion.domain.EmotionCard;
import com.muud.emotion.domain.EmotionTag;
import com.muud.emotion.domain.dto.TagListResponse;
import com.muud.emotion.domain.dto.*;
import com.muud.emotion.domain.Emotion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmotionService {

    public EmotionResponse getEmotionByName(final String emotionName) {
        return Arrays.stream(Emotion.values()).filter(emotion -> emotion.name().equalsIgnoreCase(emotionName))
                .findFirst()
                .map(EmotionResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + emotionName));
    }

    public List<EmotionResponse> getEmotionList() {
        return Arrays.stream(Emotion.values())
                .map(EmotionResponse::from)
                .collect(Collectors.toList());
    }

    public List<EmotionCountResponse> getEmotionAndCount(final List<Diary> diaryList,
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

    public List<QuestionResponse> getAllQuestionList() {
        return Arrays.stream(Emotion.values())
                .map(question -> new QuestionResponse(question.name(), question.getQuestion()))
                .collect(Collectors.toList());
    }

    /**
     * 일기 작성에 필요한 감정 리스트를 반환
     *
     * @return emotionList
     */
    public EmotionListResponse getEmotionValueList() {
        List<EmotionValue> EmotionList = Arrays.stream(EmotionCard.values())
                .map(EmotionValue::from)
                .toList();
        return EmotionListResponse.from(EmotionList);
    }

    public TagListResponse getTagListResponse(EmotionCard emotion) {
        List<String> tagList = Arrays.stream(EmotionTag.values())
                .map(EmotionTag::getName)
                .toList();
        return TagListResponse.from(tagList, getTagByEmotion(emotion));
    }

    /**
     * 파라미터로 전달받은 감정에 해당하는 focus 태그를 반환
     *
     * @param emotion 대표 감정
     * @return 태그(string)
     */
    public String getTagByEmotion(EmotionCard emotion) {
        return switch (emotion) {
            case JOY -> EmotionTag.EXCITED.getName();
            case GENERAL -> EmotionTag.RELAXED.getName();
            case ANGRY -> EmotionTag.ANNOYED.getName();
            case SADNESS -> EmotionTag.RESENTFUL.getName();
            case TIRED -> EmotionTag.PRESSURED.getName();
        };
    }
}
