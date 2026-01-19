package com.example.metroApi.Dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EstacionDto {
    private Long id;
    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;

    public EstacionDto(Long id, String nombre, BigDecimal latitud, BigDecimal longitud) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }   
}
