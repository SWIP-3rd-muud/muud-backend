package com.muud.library.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.muud.playlist.domain.dto.VideoDto;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LibraryResponse (
        Long id,
        String title,
        List<VideoDto> playLists
) {
    public LibraryResponse(Long id, String title) {
        this(id, title, null);
    }
}
