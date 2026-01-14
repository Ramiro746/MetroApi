package com.example.metroApi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.Estacion;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {

    boolean existsByGtfsId(String gtfsId);

    Optional<Estacion> findByGtfsId(String gtfsId);

}
