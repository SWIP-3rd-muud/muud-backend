package com.muud.playlist.service;

import com.muud.emotion.domain.Emotion;

public class SearchQuery {

    // 감정에 따른 검색어
    public static final String JOY_QUERY = "기쁠 때 들으면 좋은 PlayList | 기분 좋은 플리 ";
    public static final String SAD_QUERY = "슬플 때 들으면 좋은 PlayList | 슬픔 극복 플리";
    public static final String CALM_QUERY = "잔잔할 때 들으면 좋은 PlayList | 텐션업 PlayList";
    public static final String BLUE_QUERY = "우울할 때 듣는 PlayList | 위로가 되는 PlayList";
    public static final String ANGER_QUERY = "화날 때 들으면 좋은 PlayList | 진정하자 PlayList";
    public static final String TIRED_QUERY = "피곤할 때 들으면 좋은 PlayList | 잠깨는 PlayList";

    public static String getQueryByEmotion(Emotion emotion) {
        switch (emotion) {
            case JOY: return JOY_QUERY;
            case SAD: return SAD_QUERY;
            case CALM: return CALM_QUERY;
            case BLUE: return BLUE_QUERY;
            case ANGER: return ANGER_QUERY;
            case TIRED: return TIRED_QUERY;
            default: throw new IllegalArgumentException("Unknown emotion: " + emotion);
        }
    }
}
