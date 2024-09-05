package com.muud.playlist.service;

public class SearchFilter {
    public static final String EXCLUDE_RELIGIOUS_TERMS = " -교회 -찬양 -찬송 -성경 -예배 -성당 -불교 -힌두";
    public static final String EXCLUDE_POLITICAL_TERMS = " -정치 -대선 -국회 -의회";
    public static final String EXCLUDE_ADS_TERMS = " -광고 -스폰서";
    public static final String EXCLUDE_TUTORIAL_TERMS = " -튜토리얼 -가이드 -방법";
    public static final String EXCLUDE_ADULT_TERMS = " -성인 -19금";
    public static final String EXCLUDE_ALL;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(EXCLUDE_RELIGIOUS_TERMS)
                .append(EXCLUDE_POLITICAL_TERMS)
                .append(EXCLUDE_TUTORIAL_TERMS)
                .append(EXCLUDE_ADS_TERMS)
                .append(EXCLUDE_ADULT_TERMS);

        EXCLUDE_ALL = sb.toString();
    }
}
