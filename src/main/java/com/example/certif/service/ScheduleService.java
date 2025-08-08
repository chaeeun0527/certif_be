package com.example.certif.service;


import com.example.certif.dto.ScheduleDto;
import com.example.certif.entity.Schedule;
import com.example.certif.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

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
}
