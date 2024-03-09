package com.muud.report.domain.dto;

public record ReportResponse(int monthlyDiaryCount,
                             EmotionReport emotionReport,
                             PlaylistReport playlistReport
                             ) {
    public static ReportResponse of(int monthlyDiaryCount,
                                    EmotionReport emotionReport,
                                    PlaylistReport playlistReport) {
        return new ReportResponse(monthlyDiaryCount, emotionReport, playlistReport);
    }
}