package com.example.metroApi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.Linea;

@Repository
public interface LineaRepository extends JpaRepository<Linea, Long>{

    boolean existsByNombre (String nombre); 
    Optional<Linea> findByRouteId(String routeId);
}
