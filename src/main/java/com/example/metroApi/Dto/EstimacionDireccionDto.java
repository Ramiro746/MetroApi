package com.example.metroApi.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimacionDireccionDto {


    private Integer direccionId;
    private String direccionNombre;

    private Integer proximoTrenMin;   // minutos hasta que llegue
    private Integer frecuenciaMin;    // cada cuantos minutos pasa

}
