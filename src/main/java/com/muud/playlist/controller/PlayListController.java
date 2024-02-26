package com.muud.playlist.controller;

import com.muud.emotion.entity.Emotion;
import com.muud.global.error.ExceptionType;
import com.muud.global.error.ResponseError;
import com.muud.playlist.dto.PlayListResponse;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.service.PlayListService;
import com.muud.playlist.service.YoutubeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PlayListController {
    private final PlayListService playListService;
    private final YoutubeDataService youtubeDataService;
    @GetMapping("/playlists")
    public ResponseEntity<PlayListResponse> getPlayLists(@RequestParam(name = "emotion", required = true) Emotion emotion, @PageableDefault(size = 4) Pageable pageable){

        try {
            Page<VideoDto> videoDtoList = playListService.getPlayLists(emotion, pageable);
            List<String> ids = videoDtoList.map(videoDto -> videoDto.getVideoId())
                    .stream().collect(Collectors.toList());
            youtubeDataService.getVideoDetails(ids);
            return ResponseEntity.ok(PlayListResponse.from(videoDtoList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleInvalidEnumValueException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ResponseError(ExceptionType.INVALID_INPUT_VALUE.getMessage()));
    }

}
