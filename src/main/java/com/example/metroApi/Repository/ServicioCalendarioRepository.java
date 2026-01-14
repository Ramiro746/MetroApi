package com.example.metroApi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.ServicioCalendario;

@Repository
public interface ServicioCalendarioRepository extends JpaRepository<ServicioCalendario, Long> {
    Optional<ServicioCalendario> findByGtfsServiceId(String gtfsServiceId);
    boolean existsByGtfsServiceId   (String serviceId);
}
