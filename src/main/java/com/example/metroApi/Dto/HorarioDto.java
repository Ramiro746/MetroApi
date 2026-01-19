package com.example.metroApi.Dto;

import lombok.Data;

@Data

public class HorarioDto {
    private Integer arrivalMinutes;   // tiempo de llegada en minutos
    private Integer departureMinutes; // tiempo de salida en minutos
    private Long tripId;              // id del trip o gtfsTripId
    private Integer directionId;      // opcional, dirección del trip

    public HorarioDto(Integer arrivalMinutes, Integer departureMinutes, Long tripId, Integer directionId) {
        this.arrivalMinutes = arrivalMinutes;
        this.departureMinutes = departureMinutes    ;
        this.tripId = tripId;
        this.directionId = directionId;
    }

    // 🔥 Métodos derivados (no se guardan en BBDD)
    public String getArrivalTime() {
        return minutosAHora(arrivalMinutes);
    }

    public String getDepartureTime() {
        return minutosAHora(departureMinutes);
    }

    private String minutosAHora(Integer minutos) {
        int h = minutos / 60;
        int m = minutos % 60;
        return String.format("%02d:%02d", h, m);
    }
}
