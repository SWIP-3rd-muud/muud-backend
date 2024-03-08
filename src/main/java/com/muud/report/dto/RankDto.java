package com.muud.report.dto;

public record RankDto<T>(int rank,
                      T object) {
}