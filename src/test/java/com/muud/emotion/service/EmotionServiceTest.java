package com.muud.emotion.service;

import com.muud.emotion.domain.EmotionCard;
import com.muud.emotion.domain.EmotionTag;
import com.muud.emotion.domain.dto.EmotionListResponse;
import com.muud.emotion.domain.dto.TagListResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmotionServiceTest {

    @InjectMocks
    private EmotionService emotionService;

    @Test
    @DisplayName("감정들의 값을 정상적으로 호출한다")
    void getEmotionValueList_success() {
        EmotionListResponse response = emotionService.getEmotionValueList();

        assertNotNull(response);
        assertNotNull(response.emotionList());
        assertEquals(EmotionCard.values().length, response.emotionList().size());

        for (EmotionCard emotionCard : EmotionCard.values()) {
            assertTrue(response.emotionList().stream()
                    .anyMatch(emotionValue -> emotionValue.title().equals(emotionCard.getTitle())));
        }
    }

    @Test
    @DisplayName("JOY 감정의 focus 태그를 호출한다.")
    void getTagListResponse_JOY() {
        EmotionCard emotion = EmotionCard.JOY;

        TagListResponse response = emotionService.getTagListResponse(emotion);

        assertNotNull(response);
        assertNotNull(response.tagList());
        assertEquals(response.focusedTag(), EmotionTag.EXCITED.getName());
    }
}
