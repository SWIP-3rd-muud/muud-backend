package com.muud.playlist.service;

import com.muud.emotion.entity.Emotion;
import com.muud.playlist.dto.VideoDto;
import com.muud.playlist.entity.PlayList;
import com.muud.playlist.repository.PlayListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayListService {
    private final PlayListRepository playListRepository;
    public Page<VideoDto> getPlayLists(Emotion emotion, Pageable pageable){
        return playListRepository.findByEmotion(emotion, pageable)
               .map(PlayList::toDto);
    }
}