package com.muud.playlist.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
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
