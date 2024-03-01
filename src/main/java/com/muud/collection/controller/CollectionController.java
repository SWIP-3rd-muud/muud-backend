package com.muud.collection.controller;

import com.muud.auth.service.AuthService;
import com.muud.collection.dto.CollectionDto;
import com.muud.collection.entity.Collection;
import com.muud.collection.service.CollectionService;
import com.muud.global.common.PageResponse;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.service.PlayListService;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    @GetMapping("/collections")
    public ResponseEntity<PageResponse<CollectionDto>> getCollections(@RequestAttribute User user , Pageable pageable){
        Page<Collection> collectionPage = collectionService.getCollections(user, pageable);
        List<CollectionDto> collectionDtoList = collectionPage.map(collection -> collection.toDto()).toList();
        return ResponseEntity.ok(new PageResponse<>(collectionDtoList, collectionPage.getNumberOfElements()));
    }

    @PostMapping("/collections")
    public ResponseEntity addCollection(@RequestAttribute User user, @RequestParam Long playListId){
        PlayList playList = playListService.getPlayList(playListId);
        collectionService.saveCollection(user, playList.getVideoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("isCollected", true));
    }

    @GetMapping("/collections/{collectionId}")
    public ResponseEntity getCollectionDetails(@RequestAttribute User user, @PathVariable Long collectionId){
        CollectionDto collectionDto = collectionService.getCollectionDetails(user, collectionId);
        collectionDto.setPlaylist(playListService.getPlayListByVideoId(collectionDto.getVideoId()).toDto());
        return ResponseEntity.ok(collectionDto);
    }


}
