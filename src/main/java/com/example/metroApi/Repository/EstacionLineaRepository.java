package com.example.metroApi.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Dto.LineaDto;
import com.example.metroApi.Entity.Estacion;
import com.example.metroApi.Entity.EstacionLinea;

import jakarta.transaction.Transactional;

@Repository
public interface EstacionLineaRepository extends JpaRepository<EstacionLinea, Long>{
    
    @Modifying
    @Transactional
    @Query(
        value= "INSERT INTO estacion_linea (gtfs_id, direccion, order_number, estacion_id, linea_id) " +
              "SELECT CAST(st.estacion_id AS VARCHAR) || '-' || CAST(t.linea_id AS VARCHAR) AS gtfs_id,\r\n" + //
                                    "       t.direction_id AS direccion,\r\n" + //
                                    "       MIN(st.stop_sequence) AS order_number,\r\n" + //
                                    "       st.estacion_id,\r\n" + //
                                    "       t.linea_id " +
              "FROM stop_time st " +
              "JOIN trip t ON st.trip_id = t.id " +
              "GROUP BY st.estacion_id, t.linea_id, t.direction_id",
        nativeQuery= true
    )
    void poblarEstacionLinea();


    @Query("""
        SELECT DISTINCT new com.example.metroApi.Dto.LineaDto(
            el.linea.id,
            el.linea.nombre,
            el.linea.activo,
            el.estacion.nombre
        )
        FROM EstacionLinea el
        WHERE el.estacion.id = :estacionId
        ORDER BY el.linea.nombre
    """)
    List<LineaDto> findLineasPorEstacion(@Param("estacionId") Long estacionId);
}
