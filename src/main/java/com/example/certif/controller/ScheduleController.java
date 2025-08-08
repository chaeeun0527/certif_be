package com.example.certif.controller;

import com.example.certif.dto.ScheduleDto;
import com.example.certif.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    // 1. 특정 자격증 일정 조회
    @GetMapping("/api/certificates/{certificateId}/schedules")
    public ResponseEntity<List<ScheduleDto>> getSchedules(@PathVariable Long certificateId){
        List<ScheduleDto> dtos = scheduleService.getSchedules(certificateId);
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 2. 캘린더에서 자격증 일정 조회
}
