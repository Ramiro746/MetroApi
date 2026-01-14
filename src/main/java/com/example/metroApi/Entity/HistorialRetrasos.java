package com.example.metroApi.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

    @Entity
    @Data
    public class HistorialRetrasos {
        @Id
        
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "estacion_id")
        private Estacion estacion;
        @ManyToOne
        @JoinColumn(name = "linea_id")
        private Linea linea;

        private int hora;
        private int mediaRetrasoSegundos;
    }
