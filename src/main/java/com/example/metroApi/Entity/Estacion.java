package com.example.metroApi.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nose pa que XD, relacionado con el bucle for del gtfsimport
    @Column(unique = true, nullable = false)
    private String gtfsId;

    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
}
