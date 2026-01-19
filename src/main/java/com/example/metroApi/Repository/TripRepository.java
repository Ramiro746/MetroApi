package com.example.metroApi.Repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.ServicioCalendario;
import com.example.metroApi.Entity.Trip;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long>{

    Optional<Trip> findByGtfsTripId(String gtfsTripId);

    boolean existsByGtfsTripId(String gtfsTripId);

    @Query("""
    SELECT t FROM Trip t
    WHERE t.linea.id = :lineaId
    AND t.servicioCalendario IN :servicios
""")
List<Trip> findTripsActivos(
    @Param("lineaId") Long lineaId,
    @Param("servicios") List<ServicioCalendario> servicios
);
}
