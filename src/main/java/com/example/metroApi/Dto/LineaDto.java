package com.example.metroApi.Dto;

import lombok.Builder;
import lombok.Data;

    @Builder
    @Data
    public class LineaDto {
        private Long id;
        private String nombre;
        private Boolean activo;
        private String estacionNombre;
    }
