package com.muud.collection.controller;

import com.muud.auth.jwt.Auth;
import com.muud.collection.domain.dto.CollectionDto;
import com.muud.collection.service.CollectionService;
import com.muud.global.common.PageResponse;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.service.PlayListService;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    private final PlayListService playListService;

    @Auth
    @GetMapping("/collections")
    public ResponseEntity<PageResponse<CollectionDto>> getCollections(@RequestAttribute User user , Pageable pageable){
        Page<CollectionDto> collectionPage = collectionService.getCollections(user, pageable);
        return ResponseEntity.ok(new PageResponse<>(collectionPage));
    }

    @Auth
    @PostMapping("/collections")
    public ResponseEntity<CollectionDto> addCollection(@RequestAttribute User user, @RequestParam Long playListId){
        PlayList playList = playListService.getPlayList(playListId);
        CollectionDto collectionDto = collectionService.saveCollection(user, playList);
        return ResponseEntity.created(URI.create("/collections/"+collectionDto.collectionId()))
                .body(collectionDto);
    }

    @Auth
    @GetMapping("/collections/{collectionId}")
    public ResponseEntity<CollectionDto> getCollectionDetails(@RequestAttribute User user, @PathVariable Long collectionId){
        CollectionDto collectionDto = collectionService.getCollection(user, collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    @Auth
    @PatchMapping("/collections/{collectionId}/like")
    public ResponseEntity<CollectionDto> likeCollection(@RequestAttribute User user, @PathVariable Long collectionId){
        CollectionDto collectionDto = collectionService.changeLikeState(user, collectionId);
        return ResponseEntity.ok(collectionDto);
    }

    @Auth
    @GetMapping("/collections/like")
    public ResponseEntity<PageResponse<CollectionDto>> getLikedCollections(@RequestAttribute User user, Pageable pageable){
        Page<CollectionDto> collectionDtoPage = collectionService.getLikedCollections(user, pageable);
        return ResponseEntity.ok(new PageResponse<>(collectionDtoPage));
    }
}
