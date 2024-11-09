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

import static com.muud.library.exception.LibraryErrorCode.FORBIDDEN_USER;
import static com.muud.library.exception.LibraryErrorCode.LIBRARY_NOT_FOUND;
import static com.muud.playlist.exception.PlayListErrorCode.PLAY_LIST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final PlayListRepository playListRepository;
    private final LibraryMapper libraryMapper;

    /**
     * 사용자의 보관함 목록을 조회합니다.
     *
     * @param user 조회할 보관함의 소유자 (로그인한 사용자)
     * @param pageable 페이지 정보 (페이지 번호, 크기 등)
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    public Page<LibraryResponse> getLibraries(final User user,
                                              final Pageable pageable) {
        Page<Library> libraries = libraryRepository.findByOwner(user, pageable);
        return libraryMapper.toLibraryResponses(libraries);
    }

    /**
     * 사용자의 보관함을 추가합니다.
     *
     * @param user 조회할 보관함의 소유자 (로그인한 사용자)
     * @param title 보관함 타이틀
     * @param playListId 보관함 생성과 동시에 들어갈 playList Id(Nullable)
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    @Transactional
    public LibraryResponse createLibrary(final User user,
                                         final String title,
                                         final Long playListId) {
        Library library = libraryMapper.toLibrary(user, title);
        if(playListId!=null) {
            addPlayListToLibrary(library, playListId);
        }
        libraryRepository.save(library);
        return libraryMapper.toResponse(library);
    }

    /**
     * 사용자의 보관함을 삭제합니다.
     *
     * @param libraryId 삭제할 보관함 Id
     */
    @Transactional
    public void deleteLibrary(final Long libraryId) {
        Library library = getLibrary(libraryId);
        libraryRepository.delete(library);
    }

    /**
     * 사용자의 보관함 정보를 조회합니다.
     *
     * @param libraryId 조회할 보관함의 Id
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    public LibraryResponse getLibraryDetail(final Long libraryId) {
        Library library = getLibrary(libraryId);
        return libraryMapper.toResponse(library);
    }

    /**
     * 보관함을 조회하고 권한을 확인합니다.
     *
     * @param libraryId 조회할 보관함의 Id
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    public Library getLibrary(final Long libraryId) {
        Library library = libraryRepository.findById(libraryId)
                .orElseThrow(LIBRARY_NOT_FOUND::defaultException);
        if(!SecurityUtils.checkCurrentUserId(library.getOwner().getId())){
            throw FORBIDDEN_USER.defaultException();
        }
        return library;
    }

    /**
     * 보관함에 PlayList를 추가합니다.
     *
     * @param libraryId 보관함 Id
     * @param playListId 추가할 PlayList의 Id
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    @Transactional
    public LibraryResponse addPlayList(final Long libraryId,
                                       final Long playListId) {
        Library library = getLibrary(libraryId);
        addPlayListToLibrary(library, playListId);
        return libraryMapper.toResponse(library);
    }


    /**
     * 보관함에 PlayList를 추가합니다.
     *
     * @param library PlayList를 추가할 보관함
     * @param playListId 추가할 PlayList의 Id
     * @return 보관함 목록의 페이지 (LibraryResponse 형태)
     */
    public void addPlayListToLibrary(final Library library,
                                     final Long playListId) {
        PlayList playList = playListRepository.findById(playListId)
                .orElseThrow(PLAY_LIST_NOT_FOUND::defaultException);
        LibraryPlayList libraryPlayList = libraryMapper.toLibraryPlayList(library, playList);
        library.addPlayList(libraryPlayList);
    }

}
