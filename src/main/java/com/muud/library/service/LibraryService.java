package com.muud.library.service;

import com.muud.global.util.SecurityUtils;
import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.entity.LibraryPlayList;
import com.muud.library.domain.mapper.LibraryMapper;
import com.muud.library.repository.LibraryRepository;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.muud.playlist.exception.PlayListErrorCode.PLAY_LIST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final PlayListRepository playListRepository;
    private final LibraryMapper libraryMapper;

    public Page<LibraryResponse> getLibraries(User user, Pageable pageable) {
        Page<Library> libraries = libraryRepository.findByOwner(user, pageable);
        return libraries.map(libraryMapper::toResponse);
    }

    @Transactional
    public LibraryResponse createLibrary(User user, String title, Long playListId) {
        Library library = libraryMapper.toLibrary(user, title);
        if(playListId!=null) {
            PlayList playList = getPlayList(playListId);
            LibraryPlayList libraryPlayList = libraryMapper.toLibraryPlayList(library, playList);
            library.addPlayList(libraryPlayList);
        }
        libraryRepository.save(library);
        return libraryMapper.toResponse(library);
    }

    public PlayList getPlayList(Long playListId) {
        return playListRepository.findById(playListId)
                .orElseThrow(PLAY_LIST_NOT_FOUND::defaultException);
    }

    @Transactional
    public void deleteLibrary(Long libraryId) {
        Library library = getLibrary(libraryId);
        libraryRepository.delete(library);
    }

    public Library getLibrary(Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow();
        if(!SecurityUtils.checkCurrentUserId(library.getOwner().getId())){
            throw new RuntimeException();
        }
        return library;
    }
}
