package com.muud.playlist.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;

@Getter @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDto {
    private Long id;
    private String title;
    private String videoId;
    private String channelName;
    private List<String> tags;
    private String description;
}
