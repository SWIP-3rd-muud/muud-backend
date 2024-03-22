package com.muud.report.controller;

import com.muud.auth.jwt.Auth;
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

    @Auth
    @GetMapping("/reports")
    public ResponseEntity<ReportResponse> getReport(@RequestAttribute("user") final User user,
                                                    @RequestParam(name = "date", required = false) final YearMonth inputDate) {
        YearMonth date = (inputDate != null) ? inputDate : YearMonth.now();
        return ResponseEntity.ok(reportService.generate(user, date));
    }
}