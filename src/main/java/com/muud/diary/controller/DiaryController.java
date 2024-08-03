package com.muud.diary.controller;

import com.muud.auth.jwt.Auth;
import com.muud.diary.config.ImageDirectoryConfig;
import com.muud.diary.domain.dto.*;
import com.muud.diary.service.DiaryService;
import com.muud.emotion.domain.Emotion;
import com.muud.global.util.PhotoManager;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.service.PlayListService;
import com.muud.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "일기 API", description = "일기 관련 CRUD")
public class DiaryController {
    
    private final DiaryService diaryService;
    private final PhotoManager photoManager;
    private final PlayListService playListService;
    private final ImageDirectoryConfig imageDirectoryConfig;

    @Auth
    @Operation(description = "일기 내용과 사진을 입력받아 등록한다. ", summary = "일기 작성")
    @ApiResponse(responseCode = "201", description = "일기 작성 성공")
    @PostMapping("/diaries")
    public ResponseEntity<CreateDiaryResponse> createDiary(@RequestAttribute("user") final User user,
                                                           @Valid @ModelAttribute final DiaryRequest diaryRequest,
                                                           @RequestPart(name = "multipartFile", required = false) final MultipartFile multipartFile) {
        PlayList playList = playListService.getPlayList(diaryRequest.playlistId());
        String image = saveImage(multipartFile);
        CreateDiaryResponse createDiaryResponse = diaryService.create(user, diaryRequest, image, playList);
        return ResponseEntity.created(URI.create("/diaries/"+ createDiaryResponse.DiaryId()))
                .body(createDiaryResponse);
    }

    @Auth
    @Operation(description = "일기를 조회한다.", summary = "일기 상세 조회")
    @GetMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> getDiary(@RequestAttribute("user") final User user,
                                                  @PathVariable("diaryId") final Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(user, diaryId));
    }

    @Auth
    @GetMapping("/diaries/month")
    public ResponseEntity<List<DiaryResponse>> getMonthlyDiaryList(@RequestAttribute("user") final User user,
                                                                   @RequestParam(name = "date", required = true) final String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        return ResponseEntity.ok(diaryService.getMonthlyDiaryList(user, yearMonth));
    }

    @Auth
    @PutMapping("/diaries/{diaryId}")
    public ResponseEntity<DiaryResponse> updateContent(@RequestAttribute("user") final User user,
                                                       @PathVariable("diaryId") final Long diaryId,
                                                       @Valid @RequestBody final ContentUpdateRequest contentUpdateRequest) {
        return ResponseEntity.ok(diaryService.updateContent(user, diaryId, contentUpdateRequest));
    }

    @Auth
    @GetMapping("/diaries/emotion")
    public ResponseEntity<List<DiaryPreviewResponse>> getDiaryPreviewListByEmotion(@RequestAttribute("user") final User user,
                                                                                   @RequestParam(name = "emotion", required = true) final Emotion emotion) {
        return ResponseEntity.ok(diaryService.getDiaryPreviewListByEmotion(user, emotion));
    }

    private String saveImage(final MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        return photoManager.upload(image, imageDirectoryConfig.getImageDirectory());
    }
}