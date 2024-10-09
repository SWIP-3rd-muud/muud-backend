package com.muud.library.domain.dto;

import com.muud.playlist.domain.dto.VideoDto;
import java.util.List;

public record LibraryResponse (
        Long id,
        String title,
        List<VideoDto> playLists
) {
}
