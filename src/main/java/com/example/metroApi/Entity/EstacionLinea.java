package com.example.metroApi.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class EstacionLinea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "estacion_id")
    private Estacion estacion;

    /**
     stop_id del GTFS (ej: par_4_1)
     */
    @Column(name = "gtfs_id", nullable = false, updatable = false)
    private String gtfsId;

    @ManyToOne  
    @JoinColumn(name = "linea_id")
    private Linea linea;
    private int orderNumber;
    private String direccion;
}
