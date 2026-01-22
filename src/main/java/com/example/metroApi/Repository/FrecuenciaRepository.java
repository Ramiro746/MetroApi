package com.example.metroApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;  
import com.example.metroApi.Entity.Frecuencia;
import com.example.metroApi.Entity.ServicioCalendario;

@Repository
public interface FrecuenciaRepository extends JpaRepository<Frecuencia, Long> {

    @Query("""
        SELECT f
        FROM Frecuencia f
        JOIN f.trip t
        WHERE t.linea.id = :lineaId
          AND t.directionId = :direccionId
          AND f.servicio = :servicio
          AND :horaMin BETWEEN f.startTime AND f.endtTime
    """)
    List<Frecuencia> findFrecuenciasActivas(
        @Param("lineaId") Long lineaId,
        @Param("direccionId") Integer direccionId,
        @Param("servicio") ServicioCalendario servicio,
        @Param("horaMin") int horaMin
    );
}
