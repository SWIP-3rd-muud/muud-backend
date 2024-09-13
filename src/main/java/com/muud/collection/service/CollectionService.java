package com.muud.collection.service;

import com.muud.collection.domain.dto.CollectionDto;
import com.muud.collection.domain.Collection;
import com.muud.collection.repository.CollectionRepository;
import com.muud.global.error.ApiException;
import com.muud.global.error.ExceptionType;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;

    public Page<CollectionDto> getCollections(User user, Pageable pageable) {
        Page<Collection> collectionPage = collectionRepository.findByUser(user, pageable);
        return collectionPage.map(collection -> CollectionDto.of(collection.getId(), collection.isLiked(), collection.getPlayList().getVideoId()));
    }

    public CollectionDto getCollection(User user, Long collectionId){
        Collection collection = getCollectionDetails(user, collectionId);
        return CollectionDto.from(collection);
    }

    @Transactional
    public CollectionDto saveCollection(User user, PlayList playList) {
        Collection collection = collectionRepository.findByUserAndPlayList(user, playList)
                .orElse(Collection.builder().
                        user(user)
                        .playList(playList)
                        .build());
        collection = collectionRepository.save(collection);
        return CollectionDto.of(collection.getId(), collection.isLiked(), playList.getVideoId());
    }

    @Transactional
    public CollectionDto changeLikeState(User user, Long collectionId) {
        Collection collection = getCollectionDetails(user, collectionId);
        collection.changeLikeState();
        return CollectionDto.of(collection.getId(), collection.isLiked(), collection.getPlayList().getVideoId());
    }

    public Collection getCollectionDetails(User user, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new ApiException(ExceptionType.BAD_REQUEST));
        if(!user.getId().equals(collection.getUser().getId())){
            throw new ApiException(ExceptionType.FORBIDDEN_USER);
        }
        return collection;
    }

    public Page<CollectionDto> getLikedCollections(User user, Pageable pageable) {
        return collectionRepository.findByUserAndLiked(user, true, pageable)
                .map(collection -> CollectionDto.of(collection.getId(), collection.isLiked(), collection.getPlayList().getVideoId()));
    }
}
