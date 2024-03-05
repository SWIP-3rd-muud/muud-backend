package com.muud.diary.domain;

import com.muud.bookmark.domain.Bookmark;
import com.muud.emotion.entity.Emotion;
import com.muud.global.common.BaseEntity;
import com.muud.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity {

//    private static final int MAX_CONTENT_LENGTH = 200;

    @Id
    @Column(name = "diary_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Size(max = MAX_CONTENT_LENGTH)
    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;

//    @Column
//    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    private PlaylistId playlistId;

    @OneToMany(mappedBy = "diary")
    private List<Bookmark> bookmarkList;

    public Diary(String content, Emotion emotion, User user) {
        this.id = null;
        this.content = content;
        this.emotion = emotion;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
