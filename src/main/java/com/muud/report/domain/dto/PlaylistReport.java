package com.muud.report.domain.dto;

import com.muud.playlist.domain.dto.VideoDto;

import java.util.List;

public record PlaylistReport(int resultCount,
                             List<RankDto<VideoDto>> playlistCountList) {
}