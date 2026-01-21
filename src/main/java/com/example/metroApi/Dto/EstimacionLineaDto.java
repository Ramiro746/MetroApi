package com.example.metroApi.Dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstimacionLineaDto {

    private Long estacionId;
    private String estacionNombre;

    private Long lineaId;
    private String lineaNombre;

    private String horaConsulta;
    private boolean estimado;

    private List<EstimacionDireccionDto> direcciones;
}
