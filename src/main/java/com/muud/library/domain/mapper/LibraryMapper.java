package com.muud.library.domain.mapper;

import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.entity.LibraryPlayList;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.domain.dto.VideoDto;
import com.muud.user.entity.User;
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
                toLibraryPlayListsToVideoDtos(library.getLibraryPlayLists())
        );
    }

    public List<VideoDto> toLibraryPlayListsToVideoDtos(List<LibraryPlayList> libraryPlayLists) {
        if (libraryPlayLists == null) {
            return Collections.emptyList();
        }

        return libraryPlayLists.stream()
                .map(libraryPlayList -> libraryPlayList.getPlayList().toDto())
                .collect(Collectors.toList());
    }

    public Library toLibrary(User user, String title) {
        return Library.builder()
                .owner(user)
                .title(title)
                .build();
    }

    public LibraryPlayList toLibraryPlayList(Library library, PlayList playList) {
        return LibraryPlayList.builder()
                .library(library)
                .playList(playList)
                .build();
    }
}
