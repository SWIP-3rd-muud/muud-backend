package com.muud.diary.domain;

import com.muud.emotion.entity.Emotion;
import com.muud.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity {

//    private static final int MAX_CONTENT_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

//    @NotNull
//    @Size(max = MAX_CONTENT_LENGTH)
    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion")
    private Emotion emotion;

//    @Column
//    private String imageUrl;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;

//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    private PlaylistId playlistId;

//    @OneToMany(mappedBy = "diary", fetch = FetchType.LAZY)
//    private Bookmark bookmark;

    public Diary(String content, Emotion emotion) {
        this.diaryId = null;
        this.content = content;
        this.emotion = emotion;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
