package com.muud.playlist.service;

import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.domain.dto.VideoDto;
import com.muud.playlist.repository.PlayListRepository;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayListServiceTest {

    @Mock
    private PlayListRepository playListRepository;

    @InjectMocks
    private PlayListService playListService;

    Emotion emotion;
    Pageable pageable;
    PlayList playList;
    VideoDto videoDto;

    @BeforeEach
    void setUp() {
        emotion = Emotion.JOY;
        pageable = Pageable.unpaged();

        playList = PlayList.builder()
                .videoId("testVideoId")
                .emotion(emotion)
                .title("testPlayList")
                .build();

        videoDto = VideoDto.builder()
                .videoId("testVideoId")
                .title("testVideoTitle")
                .build();
    }

    @Test
    @DisplayName("감정과 관련된 플레이리스트 목록을 반환")
    public void testGetPlayListByEmotion_success() {
        // given
        Long playListId = 1L;
        Page<PlayList> playListPage = new PageImpl<>(Collections.singletonList(playList));
        Page<VideoDto> videoDtoPage = new PageImpl<>(Collections.singletonList(videoDto));
        when(playListRepository.findByEmotion(emotion, pageable)).thenReturn(playListPage);

        // when
        Page<VideoDto> result = playListService.getPlayLists(emotion, pageable);

        // then
        assertNotNull(result);
        assertEquals(playList.getVideoId(), result.getContent().get(0).getVideoId());
    }

    @Test
    @DisplayName("playListId로 playList를 조회")
    public void testGetPlayListById_success() {
        // given
        Long playListId = 1L;
        when(playListRepository.findById(playListId)).thenReturn(Optional.of(playList));

        //when
        PlayList result = playListService.getPlayList(playListId);

        assertNotNull(result);
        verify(playListRepository, times(1)).findById(playListId);
        assertEquals(playList.getVideoId(), result.getVideoId());
    }

    @Test
    @DisplayName("존재하지 않는 playListId가 넘어오면 BAD_REQUEST 예외가 발생")
    public void testGetPlayList_NotFound() {
        // given
        Long playListId = 1L;
        when(playListRepository.findById(playListId)).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> playListService.getPlayList(playListId));
        assertEquals(ExceptionType.BAD_REQUEST, exception.getExceptionType());
        verify(playListRepository, times(1)).findById(playListId);
    }

    @Test
    @DisplayName("PlayList를 삭제")
    public void testRemovePlayList_success() {
        // given
        Long playlistId = 1L;
        doNothing().when(playListRepository).deleteById(playlistId);

        // when
        playListService.removePlayList(playlistId);

        // then
        verify(playListRepository, times(1)).deleteById(playlistId);
    }
}
