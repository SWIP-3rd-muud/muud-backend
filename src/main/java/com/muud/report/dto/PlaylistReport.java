package com.muud.report.dto;

import com.muud.playlist.dto.VideoDto;

import java.util.List;

public record PlaylistReport(int resultCount,
                             List<RankDto<VideoDto>> playlistCountList) {
}