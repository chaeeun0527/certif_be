package com.example.certif.controller;

import com.example.certif.dto.ScheduleDto;
import com.example.certif.security.UserPrincipal;
import com.example.certif.service.ScheduleService;
import com.example.certif.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final JwtUtil jwtUtil;

    // 1. 특정 자격증 일정 조회
    @GetMapping("/api/certificates/{certificateId}/schedules")
    public ResponseEntity<List<ScheduleDto>> getSchedules(@PathVariable Long certificateId){
        List<ScheduleDto> dtos = scheduleService.getSchedules(certificateId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 2. 즐겨찾기 자격증 일정 조회(기간 필터링)
    // 예시 /api/schedules/favorites?startDate=2025-08-01&endDate=2025-08-31
    @GetMapping("/api/schedules/favorites")
    public ResponseEntity<List<ScheduleDto>> getFavoriteSchedules(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        Long userId = principal.getUserId();
        List<ScheduleDto> dtos = scheduleService.getFavoriteSchedulesInPeriod(userId, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}
