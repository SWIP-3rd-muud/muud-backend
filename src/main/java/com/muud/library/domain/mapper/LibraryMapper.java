package com.muud.library.domain.mapper;

import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.entity.LibraryPlayList;
import com.muud.playlist.domain.dto.VideoDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    public LibraryResponse toResponse(Library library) {
        return new LibraryResponse(
                library.getId(),
                library.getTitle(),
                mapLibraryPlayListsToVideoDtos(library.getLibraryPlayLists())
        );
    }

    public List<VideoDto> mapLibraryPlayListsToVideoDtos(List<LibraryPlayList> libraryPlayLists) {
        if (libraryPlayLists == null) {
            return Collections.emptyList();
        }

        return libraryPlayLists.stream()
                .map(libraryPlayList -> libraryPlayList.getPlayList().toDto())
                .collect(Collectors.toList());
    }

}
