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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;

    public Page<CollectionDto> getCollections(User user, Pageable pageable) {
        Page<Collection> collectionPage = collectionRepository.findByUser(user, pageable);
        return collectionPage.map(collection -> collection.toDto());
    }

    public Collection saveCollection(User user, String videoId) {
        Collection collection = collectionRepository.findByUserAndVideoId(user, videoId)
                .orElse(Collection.builder().
                        user(user)
                        .videoId(videoId)
                        .build());
        return collectionRepository.save(collection);
    }

    public Collection getCollectionDetails(User user, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
        if(!user.getId().equals(collection.getUser().getId())){
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
        return collection;
    }
}
