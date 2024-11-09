package com.muud.library.service;

import com.muud.auth.service.UserPrincipal;
import com.muud.emotion.domain.Emotion;
import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.domain.entity.LibraryPlayList;
import com.muud.library.domain.mapper.LibraryMapper;
import com.muud.library.exception.LibraryErrorCode;
import com.muud.library.exception.LibraryException;
import com.muud.library.repository.LibraryRepository;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.exception.PlayListErrorCode;
import com.muud.playlist.exception.PlayListException;
import com.muud.playlist.repository.PlayListRepository;
import com.muud.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Collections;
import java.util.Optional;
import static com.amazonaws.util.ValidationUtils.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private LibraryMapper libraryMapper;

    @Mock
    private PlayListRepository playListRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private User user;
    private UserPrincipal userPrincipal;
    private Library library;
    private PlayList playList;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .nickname("test")
                .password("encryptedPassword")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        library = Library.builder()
                .owner(user)
                .title("My Library")
                .build();

        playList = PlayList.builder()
                .title("My PlayList")
                .emotion(Emotion.JOY)
                .build();
        ReflectionTestUtils.setField(playList, "id", 1L);
    }

    private void mockSecurity() {
        userPrincipal = new UserPrincipal(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }
    @Test
    @DisplayName("user의 보관함 리스트를 가져온다.")
    void getLibraries_ReturnsLibraryResponses() {
        // given
        Pageable pageable = Pageable.ofSize(10);
        Page<Library> libraries = new PageImpl<>(Collections.singletonList(library), pageable, 1);
        LibraryResponse libraryResponse = new LibraryResponse(library.getId(), library.getTitle()); // Create LibraryResponse from Library
        when(libraryRepository.findByOwner(user, pageable)).thenReturn(libraries);
        when(libraryMapper.toLibraryResponses(libraries)).thenReturn(new PageImpl<>(Collections.singletonList(libraryResponse))); // Mock the mapper

        // when
        Page<LibraryResponse> result = libraryService.getLibraries(user, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertNotNull(result.getContent(), "The content should not be null");
        assertEquals(library.getTitle(), result.getContent().get(0).title());
    }


    @Test
    @DisplayName("보관함을 PlayList 없이 생성한다.")
    void createLibrary_CreatesLibrarySuccessfully() {
        // given
        String title = "My Library";
        when(libraryMapper.toLibrary(user, title)).thenReturn(library);
        when(libraryRepository.save(library)).thenReturn(library);
        when(libraryMapper.toResponse(library)).thenReturn(new LibraryResponse(library.getId(), library.getTitle()));

        // when
        LibraryResponse result = libraryService.createLibrary(user, title, null);

        // then
        assertEquals(title, result.title());
        assertEquals(library.getId(), result.id());
    }

    @Test
    @DisplayName("보관함 생성과 함께 플레이리스트를 추가한다.")
    void createLibraryWithPlayList_CreatesLibraryWithPlayListSuccessfully() {
        // given
        when(libraryMapper.toLibrary(user, library.getTitle())).thenReturn(library);
        when(playListRepository.findById(playList.getId())).thenReturn(Optional.of(playList));
        when(libraryRepository.save(library)).thenReturn(library);
        when(libraryMapper.toResponse(library)).thenReturn(new LibraryResponse(library.getId(), library.getTitle(), Collections.singletonList(playList.toDto())));

        // when
        LibraryResponse result = libraryService.createLibrary(user, library.getTitle(), playList.getId());

        // then
        assertEquals(playList.getTitle(), result.playLists().get(0).getTitle());
        assertEquals(library.getId(), result.id());
    }

    @Test
    @DisplayName("보관함 생성과 함께 존재하지 않는 플레이리스트를 추가하면 PlayListException::PLAY_LIST_NOT_FOUND가 발생한다")
    void createLibraryWithPlayList_PlayList_Not_Exist() {
        // given
        when(libraryMapper.toLibrary(user, library.getTitle())).thenReturn(library);
        when(playListRepository.findById(playList.getId())).thenReturn(Optional.empty());

        // when
        PlayListException exception = assertThrows(PlayListException.class, () ->
                libraryService.createLibrary(user, library.getTitle(), playList.getId())
        );

        // then
        assertEquals(exception.getErrorCode(), PlayListErrorCode.PLAY_LIST_NOT_FOUND);

    }
    @Test
    @DisplayName("보관함 정보를 조회한다.")
    void getLibraryDetail_ReturnsLibraryDetail() {
        // given
        Long libraryId = 1L;
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));
        when(libraryMapper.toResponse(library)).thenReturn(new LibraryResponse(library.getId(), library.getTitle()));
        mockSecurity();

        // when
        LibraryResponse result = libraryService.getLibraryDetail(libraryId);

        // then
        assertEquals(library.getId(), result.id());
        assertEquals(library.getTitle(), result.title());
    }

    @Test
    @DisplayName("존재하지 않는 보관함 정보를 조회하면 LibraryException::LIBRARY_NOT_FOUND가 발생한다")
    void getLibraryDetail_NotExist() {
        // given
        Long libraryId = 1L;
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.empty());

        // when
        LibraryException exception = assertThrows(LibraryException.class, () -> libraryService.getLibraryDetail(libraryId));

        // then
        assertEquals(exception.getErrorCode(), LibraryErrorCode.LIBRARY_NOT_FOUND);
    }

    @Test
    @DisplayName("보관함을 삭제한다.")
    void deleteLibrary_DeletesLibrarySuccessfully() {
        // given
        Long libraryId = 1L;
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));
        mockSecurity();

        // when
        libraryService.deleteLibrary(libraryId);

        // then
        assertEquals("My Library", library.getTitle());
    }

    @Test
    @DisplayName("보관함에 플레이리스트를 추가한다.")
    void addPlayListToLibrary_successfully() {
        // given
        Long libraryId = 1L;
        Long playListId = 1L;

        // 라이브러리 및 플레이리스트 반환 값 설정
        when(libraryRepository.findById(libraryId)).thenReturn(java.util.Optional.of(library));
        when(playListRepository.findById(playListId)).thenReturn(java.util.Optional.of(playList));
        LibraryPlayList libraryPlayList = new LibraryPlayList(library, playList);
        when(libraryMapper.toLibraryPlayList(library, playList)).thenReturn(libraryPlayList);
        when(libraryMapper.toResponse(library)).thenReturn(new LibraryResponse(libraryId, library.getTitle(), Collections.singletonList(playList.toDto())));
        library.addPlayList(libraryPlayList);
        mockSecurity();

        // when
        LibraryResponse result = libraryService.addPlayList(libraryId, playListId);

        // then
        assertEquals(libraryId, result.id(), "라이브러리 ID가 일치해야 합니다.");
        assertEquals(library.getTitle(), result.title());
        assertEquals(library.getLibraryPlayLists().get(0).getPlayList().getTitle(), playList.getTitle());
    }
}
