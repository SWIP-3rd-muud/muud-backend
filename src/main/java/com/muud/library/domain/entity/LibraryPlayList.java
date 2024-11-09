package com.muud.library.domain.entity;

import com.muud.playlist.domain.PlayList;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "library_playlist")
public class LibraryPlayList {

    @Id
    @Column(name = "library_playlist_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private PlayList playList;

    @Builder
    public LibraryPlayList(Library library, PlayList playList) {
        this.library = library;
        this.playList = playList;
    }
}
