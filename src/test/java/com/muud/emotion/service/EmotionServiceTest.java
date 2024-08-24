package com.muud.emotion.service;

import com.muud.emotion.domain.EmotionCard;
import com.muud.emotion.domain.dto.EmotionListResponse;
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
}
