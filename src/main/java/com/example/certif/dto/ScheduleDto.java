package com.example.certif.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private Long scheduleId;

    private Long certificateId;
    private String certificateName;

    private String examSession;

    private Long phaseId;
    private String phaseName;

    private Long scheduleTypeId;
    private String scheduleTypeName;

    private LocalDate stratDate;
    private LocalDate endDate;

}
