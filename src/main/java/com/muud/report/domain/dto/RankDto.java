package com.muud.report.domain.dto;

public record RankDto<T>(int rank,
                      T object) {
}