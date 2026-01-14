package com.example.metroApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.Frecuencia;

@Repository
public interface FrecuenciaRepository extends JpaRepository<Frecuencia, Long> {

}
