package com.example.metroApi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.StopTime;
import com.example.metroApi.Entity.Trip;
import com.example.metroApi.Dto.HorarioDto;
import com.example.metroApi.Entity.Estacion;


@Repository
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {
    List<StopTime> findByTripOrderByStopSequenceAsc(Trip trip);

    List<StopTime> findByEstacionAndArrivalMinutesBetween(
        Estacion estacion, int start, int end
    );


    @Query("""
        SELECT new com.example.metroApi.Dto.HorarioDto(
            st.arrivalMinutes,
            st.departureMinutes,
            t.id,
            t.directionId
        )
        FROM StopTime st
        JOIN st.trip t
        WHERE st.estacion.id = :estacionId
          AND t.linea.id = :lineaId
    """)
    List<HorarioDto> findHorariosPorEstacionYLinea(
        @Param("estacionId") Long estacionId,
        @Param("lineaId") Long lineaId
    );


    @Query("""
    SELECT new com.example.metroApi.Dto.HorarioDto(
        st.arrivalMinutes,
        st.departureMinutes,
        t.id,
        t.directionId
    )
    FROM StopTime st
    JOIN st.trip t
    WHERE st.estacion.id = :estacionId
    AND t IN :trips
""")
List<HorarioDto> findHorarios(
    Long estacionId,
    List<Trip> trips
);
}
