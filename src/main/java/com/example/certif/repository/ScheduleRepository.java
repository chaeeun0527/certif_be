package com.example.certif.repository;

import com.example.certif.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 1. 특정 자격증 일정 조회
    List<Schedule> findByCertificateId(Long certificateId);

    // 2. 즐겨찾기 일정 조회(네이티브 sql, 기간 필터링)
    @Query(value = """
            SELECT s.id AS scheduleId,
                   c.id AS certificateId,
                   c.name AS certificateName,
                   s.exam_session AS examSession,
                   p.id AS phaseId,
                   p.name AS phaseName,
                   st.id AS scheduleTypeId,
                   st.name AS scheduleTypeName,
                   s.start_date AS startDate,
                   s.end_date AS endDate
            FROM schedules s
            JOIN certificates c ON s.certificate_id = c.id
            JOIN favorites f ON f.certificate_id = c.id
            JOIN phases p ON s.phase_id = p.id
            JOIN schedule_types st ON s.type_id = st.id
            WHERE f.user_id = :userId
            AND s.start_date >= :startDate
            AND s.end_date <= :endDate
            ORDER BY s.start_date ASC
            """, nativeQuery = true)
    List<Map<String, Object>> findFavoritesSchedulesNative(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
            );
}
