package com.muud.library.service;

import com.muud.library.domain.dto.LibraryResponse;
import com.muud.library.domain.entity.Library;
import com.muud.library.repository.LibraryRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private LibraryRepository libraryRepository;

    private User user;
    private Library library;

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
    }

    @Test
    @DisplayName("user의 보관함 리스트를 가져온다.")
    void getLibraries_ReturnsLibraryResponses() {
        //given
        Pageable pageable = Pageable.ofSize(10);
        Page<Library> mockPage = new PageImpl<>(Collections.singletonList(library), pageable, 1);
        when(libraryRepository.findByOwner(user, pageable)).thenReturn(mockPage);

        // when
        Page<LibraryResponse> result = libraryService.getLibraries(user, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("My Library", result.getContent().get(0).title());
    }
}
