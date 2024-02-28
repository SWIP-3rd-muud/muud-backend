package com.muud.collection.service;

import com.muud.collection.entity.Collection;
import com.muud.collection.repositoty.CollectionRepository;
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

    public void saveCollection(User user, PlayList playList) {
        if(collectionRepository.findByUserAndPlayList(user, playList).isPresent()){
            return;
        }
        Collection collection = Collection.builder().
                user(user)
                .playList(playList)
                .build();
        collectionRepository.save(collection);
    }
}
