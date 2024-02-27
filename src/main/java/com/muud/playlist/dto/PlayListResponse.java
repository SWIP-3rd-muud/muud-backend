package com.muud.playlist.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlayListResponse {
    private int totalCount;
    private List<VideoDto> playlists = new ArrayList<>();

    @Builder
    public PlayListResponse(int totalCount, List<VideoDto> playlists) {
        this.totalCount = totalCount;
        this.playlists = playlists;
    }
    public static PlayListResponse from(Page<VideoDto> videoDtoPage){
        return PlayListResponse.builder()
                .totalCount(videoDtoPage.getSize())
                .playlists(videoDtoPage.stream().toList())
                .build();
    }
}
