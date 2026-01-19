package com.example.metroApi.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Dto.EstacionDto;
import com.example.metroApi.Entity.Estacion;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {

    boolean existsByGtfsId(String gtfsId);

    Optional<Estacion> findByGtfsId(String gtfsId);

    //Listar todas las estaciones
    @Query("""
        SELECT new com.example.metroApi.Dto.EstacionDto(
            e.id,
            e.nombre,
            e.latitud,
            e.longitud
        )
        FROM Estacion e
        ORDER BY e.nombre
    """)
    List<EstacionDto> findAllDto();

    //Obtener estacion por nombre
    @Query("""
        SELECT new com.example.metroApi.Dto.EstacionDto(
            e.id,
            e.nombre,
            e.latitud,
            e.longitud
        )
        FROM Estacion e
        WHERE e.nombre = :nombre
    """)
    List<EstacionDto> findDTOByNombre(@Param("nombre") String nombre);

}
