package com.muud.library.controller;

import com.muud.collection.domain.dto.CollectionDto;
import com.muud.global.common.PageResponse;
import com.muud.global.util.SecurityUtils;
import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.service.LibraryService;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Tag(name = "보관함 API", description = "보관함 관련 CRUD")
public class LibraryController {

    private final LibraryService libraryService;

    @Operation(description = "사용자의 보관함 리스트를 조회한다. ", summary = "보관함 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/libraries")
    public ResponseEntity<PageResponse<LibraryResponse>> getLibraries(Pageable pageable){
        Page<LibraryResponse> libraryResponses = libraryService.getLibraries(SecurityUtils.getCurrentUser(), pageable);
        return ResponseEntity.ok(new PageResponse<>(libraryResponses));
    }

    @Operation(description = "보관함을 생성한다.", summary = "보관함 추가")
    @ApiResponse(responseCode = "201", description = "보관함 추가 성공")
    @PostMapping("/libraries")
    public ResponseEntity<LibraryResponse> createLibrary(@RequestParam String title,
                                                         @RequestParam(required = false) Long playListId){
        LibraryResponse libraryResponse = libraryService.createLibrary(SecurityUtils.getCurrentUser(), title, playListId);
        return ResponseEntity.created(URI.create("/libraries/"+libraryResponse.id()))
                .body(libraryResponse);
    }

    @Operation(description = "보관함을 삭제한다.", summary = "보관함 삭제")
    @ApiResponse(responseCode = "204", description = "보관함 삭제 성공")
    @DeleteMapping("/libraries/{libraryId}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long libraryId){
        libraryService.deleteLibrary(libraryId);
        return ResponseEntity.noContent().build();
    }

}
