package com.example.metroApi.Entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class StopTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    private Trip trip;

    @ManyToOne (optional=false)
    private Estacion estacion;

    private int arrivalMinutes;
    private int departureMinutes;

    private int stopSequence;
}
