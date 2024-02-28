package com.muud.collection.entity;

import com.muud.collection.dto.CollectionDto;
import com.muud.global.common.BaseEntity;
import com.muud.playlist.entity.PlayList;
import com.muud.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "collections")
public class Collection extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private PlayList playList;
    private boolean liked;
    @Builder
    public Collection(User user, PlayList playList, boolean like) {
        this.user = user;
        this.playList = playList;
        this.liked = like;
    }

    public CollectionDto toDto(){
        return CollectionDto.builder()
                .collectionId(id)
                .videoId(playList.getVideoId())
                .like(liked)
                .build();
    }
}
