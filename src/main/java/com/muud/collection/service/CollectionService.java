package com.muud.collection.service;

import com.muud.collection.dto.CollectionDto;
import com.muud.collection.entity.Collection;
import com.muud.collection.repositoty.CollectionRepository;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.entity.PlayList;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;

    public Page<Collection> getCollections(User user, Pageable pageable) {
        return collectionRepository.findByUser(user, pageable);
    }

    public void saveCollection(User user, String videoId) {
        if(collectionRepository.findByUserAndVideoId(user, videoId).isPresent()){
            return;
        }
        Collection collection = Collection.builder().
                user(user)
                .videoId(videoId)
                .build();
        collectionRepository.save(collection);
    }

    public CollectionDto getCollectionDetails(User user, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
        if(!user.getId().equals(collection.getUser().getId())){
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
        return collection.toDto();
    }
}
