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
        String emotionEnum = "EMOTION4";

        // When
        EmotionResponse emotionResponse = emotionService.getEmotionResponse(emotionEnum);

        // Then
        assertNotNull(emotionResponse);
        assertEquals("안개주의보", emotionResponse.combinedName());
        assertEquals("온통 회색빛인 마음에 안개가 뒤덮였어요", emotionResponse.description());
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
        assertEquals("우르르 쾅쾅", emotionResponseList.get(1).combinedName());
        assertEquals(6, emotionResponseList.size());
    }
}