package com.muud.playlist.domain.dto;

import com.muud.emotion.domain.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PlayListRequest {

    @Schema(description = "플레이리스트 데이터", example = "{\"emotion\": [\"videoId\"]}")
    Map<Emotion, List<String>> playLists;

}
