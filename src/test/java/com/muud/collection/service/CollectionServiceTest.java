package com.muud.collection.service;

import com.muud.collection.domain.Collection;
import com.muud.collection.domain.dto.CollectionDto;
import com.muud.collection.repository.CollectionRepository;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CollectionServiceTest {

    @InjectMocks
    private CollectionService collectionService;

    @Mock
    private CollectionRepository collectionRepository;

    private User user;
    private PlayList playList;
    private Collection collection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .email("test@test.com")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        playList = PlayList.builder()
                .videoId("video123")
                .build();
        ReflectionTestUtils.setField(playList, "id", 1L);

        collection = Collection.builder()
                .user(user)
                .playList(playList)
                .like(false)
                .build();
        ReflectionTestUtils.setField(collection, "id", 1L);
    }

    @Test
    @DisplayName("사용자 컬렉션 목록을 페이징하여 가져오기")
    void testGetCollections() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Collection> collectionPage = new PageImpl<>(Collections.singletonList(collection));
        when(collectionRepository.findByUser(user, pageable)).thenReturn(collectionPage);

        //when
        Page<CollectionDto> result = collectionService.getCollections(user, pageable);

        //then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("video123", result.getContent().get(0).videoId());
    }

    @Test
    @DisplayName("사용자 권한이 있을 때 컬렉션 상세 정보 가져오기")
    void testGetCollectionDetails_Success() {
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        Collection result = collectionService.getCollectionDetails(user, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("video123", result.getPlayList().getVideoId());
    }

    @Test
    @DisplayName("사용자 권한이 없을 때 FORBIDDEN 예외 발생")
    void testGetCollectionDetails_Failure_Forbidden() {
        // given
        User anotherUser = User.builder().build();
        ReflectionTestUtils.setField(anotherUser, "id", 2L);
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        //when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            collectionService.getCollectionDetails(anotherUser, 1L);
        });
        assertEquals(ExceptionType.FORBIDDEN_USER, exception.getExceptionType());
    }

    @Test
    @DisplayName("PlayList에 대해 새로운 컬렉션을 저장하거나 기존 컬렉션을 반환")
    void testSaveCollection_New() {
        // given
        when(collectionRepository.findByUserAndPlayList(user, playList)).thenReturn(Optional.of(collection));
        when(collectionRepository.save(any(Collection.class))).thenReturn(collection);

        // when
        CollectionDto result = collectionService.saveCollection(user, playList);

        // then
        assertNotNull(result);
        assertEquals(1L, result.collectionId());
        assertEquals("video123", result.videoId());
        verify(collectionRepository).save(any(Collection.class));
    }

    @Test
    @DisplayName("좋아요 상태 변경")
    void testChangeLikeState() {
        // given
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));

        // when
        CollectionDto result = collectionService.changeLikeState(user, 1L);

        // then
        assertNotNull(result);
        assertTrue(result.like());
    }

    @Test
    @DisplayName("좋아요 상태의 컬렉션 목록 가져오기")
    void testGetLikedCollections() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        collection.changeLikeState();

        Page<Collection> collectionPage = new PageImpl<>(Collections.singletonList(collection));
        when(collectionRepository.findByUserAndLiked(user, true, pageable)).thenReturn(collectionPage);

        // when
        Page<CollectionDto> result = collectionService.getLikedCollections(user, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getContent().get(0).like());
    }
}
