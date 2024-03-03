package com.muud.collection.controller;

import com.muud.auth.jwt.Auth;
import com.muud.collection.dto.CollectionDto;
import com.muud.collection.entity.Collection;
import com.muud.collection.service.CollectionService;
import com.muud.global.common.PageResponse;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.service.PlayListService;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Controller
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
    public ResponseEntity addCollection(@RequestAttribute User user, @RequestParam Long playListId){
        PlayList playList = playListService.getPlayList(playListId);
        Collection collection = collectionService.saveCollection(user, playList.getVideoId());
        return ResponseEntity.created(URI.create("/collections/"+collection.getId()))
                .body(collection.toDto());
    }
    @Auth
    @GetMapping("/collections/{collectionId}")
    public ResponseEntity getCollectionDetails(@RequestAttribute User user, @PathVariable Long collectionId){
        CollectionDto collectionDto = collectionService.getCollectionDetails(user, collectionId).toDto();
        collectionDto.setPlaylist(playListService.getPlayListByVideoId(collectionDto.getVideoId()).toDto());
        return ResponseEntity.ok(collectionDto);
    }


}
