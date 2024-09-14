package com.muud.collection.controller;

import com.muud.collection.domain.dto.CollectionDto;
import com.muud.collection.service.CollectionService;
import com.muud.global.common.PageResponse;
import com.muud.global.util.SecurityUtils;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.service.PlayListService;
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
@Tag(name = "컬렉션 API", description = "컬렉션 관련 CRUD")
public class CollectionController {

    private final CollectionService collectionService;
    private final PlayListService playListService;

    @Operation(description = "사용자의 컬렉션 리스트를 조회한다. ", summary = "컬렉션 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/collections")
    public ResponseEntity<PageResponse<CollectionDto>> getCollections(Pageable pageable){
        User user = SecurityUtils.getCurrentUser();
        Page<CollectionDto> collectionPage = collectionService.getCollections(user, pageable);
        return ResponseEntity.ok(new PageResponse<>(collectionPage));
    }

    @Operation(description = "Playlist Id를 받아 컬렉션에 추가한다. ", summary = "컬렉션 추가")
    @ApiResponse(responseCode = "201", description = "컬렉션 추가 성공")
    @PostMapping("/collections")
    public ResponseEntity<CollectionDto> addCollection(@RequestParam Long playListId){
        User user = SecurityUtils.getCurrentUser();
        PlayList playList = playListService.getPlayList(playListId);
        CollectionDto collectionDto = collectionService.saveCollection(user, playList);
        return ResponseEntity.created(URI.create("/collections/"+collectionDto.collectionId()))
                .body(collectionDto);
    }

    @Operation(description = "Collection Id를 받아 컬렉션 상세 정보를 조회한다. ", summary = "컬렉션 상세 정보 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<CollectionDto> getCollectionDetails(@PathVariable Long collectionId){
        User user = SecurityUtils.getCurrentUser();
        CollectionDto collectionDto = collectionService.getCollection(user, collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    @Operation(description = "Collection Id를 받아 컬렉션 좋아요 상태를 변경한다. ", summary = "컬렉션 좋아요 상태 변경")
    @ApiResponse(responseCode = "200")
    @PatchMapping("/collections/{collectionId}/like")
    public ResponseEntity<CollectionDto> likeCollection(@PathVariable Long collectionId){
        User user = SecurityUtils.getCurrentUser();
        CollectionDto collectionDto = collectionService.changeLikeState(user, collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    @Operation(description = "좋아요 누른 Collection 리스트를 조회한다.", summary = "좋아요 누른 컬렉션 리스트 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping("/collections/like")
    public ResponseEntity<PageResponse<CollectionDto>> getLikedCollections(Pageable pageable){
        User user = SecurityUtils.getCurrentUser();
        Page<CollectionDto> collectionDtoPage = collectionService.getLikedCollections(user, pageable);
        return ResponseEntity.ok(new PageResponse<>(collectionDtoPage));
    }
}
