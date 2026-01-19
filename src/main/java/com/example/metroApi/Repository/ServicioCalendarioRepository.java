package com.example.metroApi.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.ServicioCalendario;

@Repository
public interface ServicioCalendarioRepository extends JpaRepository<ServicioCalendario, Long> {
    Optional<ServicioCalendario> findByGtfsServiceId(String gtfsServiceId);
    boolean existsByGtfsServiceId   (String serviceId);

    @Query("""
    SELECT s FROM ServicioCalendario s
    WHERE :today BETWEEN s.startDate AND s.endDate
    AND (
        (:day = 'MONDAY' AND s.monday = true) OR
        (:day = 'TUESDAY' AND s.tuesday = true) OR
        (:day = 'WEDNESDAY' AND s.wednesday = true) OR
        (:day = 'THURSDAY' AND s.thursday = true) OR
        (:day = 'FRIDAY' AND s.friday = true) OR
        (:day = 'SATURDAY' AND s.saturday = true) OR
        (:day = 'SUNDAY' AND s.sunday = true)
    )
""")
List<ServicioCalendario> findServiciosActivosHoy(
    @Param("today") LocalDate today,
    @Param("day") String day
);
}
