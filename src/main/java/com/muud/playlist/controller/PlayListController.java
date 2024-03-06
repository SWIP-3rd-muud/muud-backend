package com.muud.playlist.controller;

import com.muud.auth.jwt.Auth;
import com.muud.emotion.entity.Emotion;
import com.muud.global.common.PageResponse;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.global.error.ResponseError;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.service.PlayListService;
import com.muud.playlist.service.YoutubeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PlayListController {
    private final PlayListService playListService;
    private final YoutubeDataService youtubeDataService;
    @Value("${admin-code}")
    private String ADMIN_CODE;
    @GetMapping("/playlists")
    public ResponseEntity<PageResponse> getPlayLists(@RequestParam(name = "emotion") Emotion emotion, @PageableDefault(size = 4) Pageable pageable){
        Page<VideoDto> videoDtoList = playListService.getPlayLists(emotion, pageable);
        return ResponseEntity.ok(new PageResponse<>(videoDtoList));
    }

    @Auth
    @PostMapping("/playlists")
    public ResponseEntity updatePlayLists(@RequestBody Map<String, String> code){
        String admin_code = code.get("code");
        if(admin_code==null)
            throw new ApiException(ExceptionType.BAD_REQUEST);
        if(ADMIN_CODE.equals(admin_code)){
            try {
                int resultCount = youtubeDataService.updateVideoList();
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("resultCount", resultCount));
            } catch (IOException e) {
                throw new ApiException(ExceptionType.BAD_REQUEST);
            }
        }else{
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseError> handleInvalidEnumValueException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(new ResponseError(ExceptionType.INVALID_INPUT_VALUE.getMessage()));
    }


}
