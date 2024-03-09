package com.muud.report.service;

import com.muud.diary.domain.Diary;
import com.muud.diary.repository.DiaryRepository;
import com.muud.emotion.domain.Emotion;
import com.muud.playlist.entity.PlayList;
import com.muud.report.domain.dto.RankDto;
import com.muud.playlist.dto.VideoDto;
import com.muud.report.domain.dto.EmotionReport;
import com.muud.report.domain.dto.PlaylistReport;
import com.muud.report.domain.dto.ReportResponse;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final DiaryRepository diaryRepository;

    private static final int INITIAL_RANK = 1;
    private static final int RANK_LIMIT = 5;

    public ReportResponse generate(final User user,
                                   final YearMonth date) {
        List<Diary> diaryList = diaryRepository.findByMonthAndYear(user.getId(), date.getMonthValue(), date.getYear());
        return ReportResponse.of(getMonthlyDiaryCount(diaryList),
                getEmotionReport(diaryList),
                getPlaylistReport(diaryList));
    }

    private int getMonthlyDiaryCount(final List<Diary> diaryList) {
        return diaryList.size();
    }

    private EmotionReport getEmotionReport(final List<Diary> diaryList) {
        AtomicInteger rankCounter = new AtomicInteger(INITIAL_RANK);
        List<RankDto<String>> emotionRankList = diaryList.stream()
                .collect(Collectors.groupingBy(diary -> diary.getEmotion(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Emotion, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(RANK_LIMIT)
                .map(entry -> new RankDto<>(rankCounter.getAndIncrement(), entry.getKey().getTitleEmotion()))
                .collect(Collectors.toList());
        return new EmotionReport(emotionRankList.size(), emotionRankList);
    }

    private PlaylistReport getPlaylistReport(final List<Diary> diaryList) {
        AtomicInteger rankCounter = new AtomicInteger(INITIAL_RANK);
        List<RankDto<VideoDto>> playlistRankList = diaryList.stream()
                .collect(Collectors.groupingBy(diary -> diary.getPlayList(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<PlayList, Long> comparingByValue(Comparator.reverseOrder()))
                .limit(RANK_LIMIT)
                .map(entry -> new RankDto<>(rankCounter.getAndIncrement(), entry.getKey().toDto()))
                .collect(Collectors.toList());
        return new PlaylistReport(playlistRankList.size(), playlistRankList);
    }
}