package com.muud.library.domain.mapper;

import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.entity.LibraryPlayList;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.domain.dto.VideoDto;
import com.muud.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class LibraryMapper {

    public LibraryResponse toResponse(final Library library) {
        return new LibraryResponse(
                library.getId(),
                library.getTitle(),
                toLibraryPlayListsToVideoDtos(library.getLibraryPlayLists())
        );
    }

    public Page<LibraryResponse> toLibraryResponses(final Page<Library> libraries) {
        return libraries.map(library -> new LibraryResponse(
                library.getId(),
                library.getTitle()
        ));
    }

    public List<VideoDto> toLibraryPlayListsToVideoDtos(final List<LibraryPlayList> libraryPlayLists) {
        return Stream.ofNullable(libraryPlayLists)
                .flatMap(List::stream)
                .map(libraryPlayList -> libraryPlayList.getPlayList().toDto())
                .collect(Collectors.toList());
    }

    public Library toLibrary(final User user, final String title) {
        return Library.builder()
                .owner(user)
                .title(title)
                .build();
    }

    public LibraryPlayList toLibraryPlayList(final Library library, final PlayList playList) {
        return LibraryPlayList.builder()
                .library(library)
                .playList(playList)
                .build();
    }
}
