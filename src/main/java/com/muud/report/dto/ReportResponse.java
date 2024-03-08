package com.muud.report.dto;

public record ReportResponse(int diaryCount,
                             EmotionReport emotionReport,
                             PlaylistReport playlistReport
                             ) {
    public static ReportResponse of(int diaryCount,
                                    EmotionReport emotionReport,
                                    PlaylistReport playlistReport) {
        return new ReportResponse(diaryCount, emotionReport, playlistReport);
    }
}