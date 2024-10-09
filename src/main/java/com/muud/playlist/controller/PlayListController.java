package com.muud.playlist.controller;

import com.muud.emotion.domain.Emotion;
import com.muud.global.common.PageResponse;
import com.muud.playlist.domain.dto.PlayListRequest;
import com.muud.playlist.domain.dto.VideoDto;
import com.muud.playlist.service.PlayListService;
import com.muud.playlist.service.YoutubeDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PlayList API", description = "PlayList 관련 API")
public class PlayListController {

    private final PlayListService playListService;
    private final YoutubeDataService youtubeDataService;

    @Operation(description = "감정과 페이징 정보를 받아 감정과 관련된 PlayList를 랜덤으로 조회합니다.", summary = "플레이 리스트 목록 조회")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청값")
    @GetMapping("/playlists")
    public ResponseEntity<PageResponse> getPlayLists(@RequestParam(name = "emotion") Emotion emotion, @PageableDefault(size = 4) Pageable pageable){
        Page<VideoDto> videoDtoList = playListService.getPlayLists(emotion, pageable);
        return ResponseEntity.ok(new PageResponse<>(videoDtoList));
    }

    @Operation(description = "관리자 권한으로 PlayList를 DB에 insert/update 해줍니다.", summary = "플레이 리스트 갱신")
    @ApiResponse(responseCode = "200", description = "데이터 갱신 성공")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    @PostMapping("/playlists/data")
    public ResponseEntity updatePlayLists(){
        youtubeDataService.upsertPlayList();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "success"));
    }

    @Operation(description = "관리자 권한으로 특정 PlayList들을 생성합니다.", summary = "플레이 리스트 추가")
    @ApiResponse(responseCode = "201", description = "PlayList 추가 성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청값")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    @ApiResponse(responseCode = "500", description = "서버 에러")
    @PostMapping("/playlists")
    public ResponseEntity addPlayList(@RequestBody PlayListRequest playListRequestList){
        youtubeDataService.addPlayList(playListRequestList);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "success"));

    }

    @Operation(description = "관리자 권한으로 특정 PlayList를 삭제합니다.", summary = "플레이 리스트 삭제")
    @ApiResponse(responseCode = "204", description = "PlayList 삭제 성공")
    @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    @DeleteMapping("/playlists/{playlistId}")
    public ResponseEntity deletePlayList(@PathVariable Long playlistId){
        playListService.removePlayList(playlistId);
        return ResponseEntity.noContent().build();
    }

}
