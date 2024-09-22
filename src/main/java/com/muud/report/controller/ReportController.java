package com.muud.report.controller;

import com.muud.global.util.SecurityUtils;
import com.muud.report.domain.dto.ReportResponse;
import com.muud.report.service.ReportService;
import com.muud.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<ReportResponse> getReport(@RequestParam(name = "date", required = false) final YearMonth inputDate) {
        User user = SecurityUtils.getCurrentUser();
        YearMonth date = (inputDate != null) ? inputDate : YearMonth.now();
        return ResponseEntity.ok(reportService.generate(user, date));
    }
}