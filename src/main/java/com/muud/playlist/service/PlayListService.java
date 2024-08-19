package com.muud.playlist.service;

import com.muud.emotion.domain.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.domain.dto.VideoDto;
import com.muud.playlist.domain.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class PlayListService {

    private final PlayListRepository playListRepository;

    public Page<VideoDto> getPlayLists(Emotion emotion, Pageable pageable){
        return playListRepository.findByEmotion(emotion, pageable)
               .map(PlayList::toDto);
    }

    public PlayList getPlayList(Long playListId) {
        return playListRepository.findById(playListId)
                .orElseThrow(()-> new ApiException(ExceptionType.BAD_REQUEST));
    }

    public void removePlayList(Long playlistId) {
        playListRepository.deleteById(playlistId);
    }
}
