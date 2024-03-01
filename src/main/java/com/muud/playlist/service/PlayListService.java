package com.muud.playlist.service;

import com.muud.emotion.entity.Emotion;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public PlayList getPlayListByVideoId(String videoId) {
        return playListRepository.findByVideoId(videoId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
    }
}
