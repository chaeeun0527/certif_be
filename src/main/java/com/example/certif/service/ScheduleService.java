package com.example.certif.service;


import com.example.certif.dto.ScheduleDto;
import com.example.certif.entity.Schedule;
import com.example.certif.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 1. 특정 자격증 일정 조회
    public List<ScheduleDto> getSchedules(Long certificateId) {
        List<Schedule> schedules = scheduleRepository.findByCertificateId(certificateId);
        return schedules.stream()
                .map(s -> new ScheduleDto(
                        s.getId(),
                        s.getCertificate().getId(),
                        s.getCertificate().getName(),
                        s.getExamSession(),
                        s.getPhase().getId(),
                        s.getPhase().getName(),
                        s.getScheduleType().getId(),
                        s.getScheduleType().getName(),
                        s.getStartDate(),
                        s.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    // 2. 즐겨찾기 자격증 일정 조회
    public List<ScheduleDto> getFavoriteSchedulesInPeriod(
            Long userId, LocalDate startDate, LocalDate endDate
    ) {

        List<Map<String, Object>> rows = scheduleRepository.findFavoritesSchedulesNative(userId, startDate, endDate);

        return rows.stream()
                .map(r -> new ScheduleDto(
                        ((Number) r.get("scheduleId")).longValue(),
                        ((Number) r.get("certificateId")).longValue(),
                        (String) r.get("certificateName"),
                        (String) r.get("examSession"),
                        ((Number) r.get("phaseId")).longValue(),
                        (String) r.get("phaseName"),
                        ((Number) r.get("scheduleTypeId")).longValue(),
                        (String) r.get("scheduleTypeName"),
                        ((java.sql.Date) r.get("startDate")).toLocalDate(),
                        ((java.sql.Date) r.get("endDate")).toLocalDate()
                ))
                .toList();
    }
}

