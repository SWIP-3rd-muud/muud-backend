package com.muud.collection.domain;

import com.muud.collection.domain.dto.CollectionDto;
import com.muud.global.common.BaseEntity;
import com.muud.playlist.entity.PlayList;
import com.muud.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @Getter
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

    public CollectionDto toDetailDto(){
        return CollectionDto.builder()
                .collectionId(id)
                .like(liked)
                .playList(playList.toDto())
                .build();
    }
    public CollectionDto toDto(){
        return CollectionDto.builder()
                .collectionId(id)
                .like(liked)
                .videoId(playList.getVideoId())
                .build();
    }
    public void changeLikeState() {
        this.liked = !this.liked;
    }
}
