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
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String gtfsTripId;

    @ManyToOne
    @JoinColumn(name = "linea_id", nullable = false)
    private Linea linea;

    @ManyToOne
    @JoinColumn(name = "servicio_id", nullable = false)
    private ServicioCalendario servicioCalendario;

    private Integer directionId;
}
