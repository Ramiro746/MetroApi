package com.example.metroApi.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimacionDto {

    private Long estacionId;
    private String estacionNombre;

    private Long lineaId;
    private String lineaNombre;

    private Integer direccionId;
    private String direccionNombre;

    private Integer proximoTrenMin;   // minutos hasta que llegue
    private Integer frecuenciaMin;    // cada cuantos minutos pasa

    private boolean estimado;          // true siempre (por ahora)

    private String horaConsulta;
}
