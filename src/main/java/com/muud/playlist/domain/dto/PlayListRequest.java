package com.muud.playlist.domain.dto;

import com.muud.emotion.domain.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import static com.muud.playlist.exception.PlayListErrorCode.INVALID_REQUEST;

public record PlayListRequest(
        @Schema(description = "플레이리스트 데이터",
                example = "{\"JOY\": [\"videoId\"]}")
        Map<Emotion, List<String>> playLists) {

    public PlayListRequest(Map<Emotion, List<String>> playLists) {
        for(Emotion emotion: playLists.keySet()){
            validateEmotionName(emotion.name());
        }
        this.playLists = playLists;
    }

    private void validateEmotionName(String emotionName) {
        if(!Emotion.isValidEmotionName(emotionName)){
            throw INVALID_REQUEST.defaultException();
        }
    }
}
