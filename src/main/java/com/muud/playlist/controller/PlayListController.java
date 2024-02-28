package com.muud.playlist.controller;

import com.muud.emotion.entity.Emotion;
import com.muud.global.common.PageResponse;
import com.muud.global.error.ExceptionType;
import com.muud.global.error.ResponseError;
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

@Controller
@RequiredArgsConstructor
public class PlayListController {
    private final PlayListService playListService;
    private final YoutubeDataService youtubeDataService;
    @GetMapping("/playlists")
    public ResponseEntity<PageResponse> getPlayLists(@RequestParam(name = "emotion", required = true) Emotion emotion, @PageableDefault(size = 4) Pageable pageable){
        Page<VideoDto> videoDtoList = playListService.getPlayLists(emotion, pageable);
        return ResponseEntity.ok(new PageResponse<>(videoDtoList));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleInvalidEnumValueException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ResponseError(ExceptionType.INVALID_INPUT_VALUE.getMessage()));
    }

}
