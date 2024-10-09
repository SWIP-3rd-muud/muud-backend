package com.muud.library.service;

import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.mapper.LibraryMapper;
import com.muud.library.repository.LibraryRepository;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final LibraryMapper libraryMapper;

    public Page<LibraryResponse> getLibraries(User user, Pageable pageable) {
        Page<Library> libraries = libraryRepository.findByOwner(user, pageable);
        return libraries.map(libraryMapper::toResponse);
    }

}
