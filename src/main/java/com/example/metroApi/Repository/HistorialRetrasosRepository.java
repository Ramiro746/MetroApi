package com.example.metroApi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.HistorialRetrasos;

@Repository
public interface HistorialRetrasosRepository extends JpaRepository<HistorialRetrasos, Long> {

}
