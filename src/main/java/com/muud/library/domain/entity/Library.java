package com.muud.library.domain.entity;

import com.muud.global.common.BaseEntity;
import com.muud.playlist.domain.PlayList;
import com.muud.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Library extends BaseEntity {

    @Id
    @Column(name = "library_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<LibraryPlayList> libraryPlayLists = new ArrayList<>();

    @Builder
    public Library(String title, User owner, List<LibraryPlayList> libraryPlayLists) {
        this.title = title;
        this.owner = owner;
        this.libraryPlayLists = libraryPlayLists;
    }

    public void addPlayList(LibraryPlayList playList) {
        if(libraryPlayLists==null)
            libraryPlayLists = new ArrayList<>();
        libraryPlayLists.add(playList);
    }
}
