package com.muud.emotion.service;

import com.muud.emotion.dto.EmotionResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmotionServiceTest {

    private EmotionService emotionService = new EmotionService();

    @Test
    void getEmotion() {
        // Given
        String emotionEnum = "EMOTION1";

        // When
        EmotionResponse emotionResponse = emotionService.getEmotionResponse(emotionEnum);

        // Then
        assertNotNull(emotionResponse);
        assertEquals("호우주의보", emotionResponse.combinedName());
        assertEquals("조용하고 잔잔한 봄날의 그늘", emotionResponse.description());
    }

    @Test
    void getEmotion_InvalidType_ThrowsException() {
        // Given
        String emotionEnum = "INVALID_TYPE";

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            // When
            emotionService.getEmotionResponse(emotionEnum);
        });
    }

    @Test
    void getEmotionList() {
        // When
        List<EmotionResponse> emotionResponseList = emotionService.getEmotionResponseList();

        // Then
        assertNotNull(emotionResponseList);
        assertEquals("덤덤하네", emotionResponseList.get(1).combinedName());
        assertEquals(2, emotionResponseList.size());
    }
}