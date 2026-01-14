package com.example.metroApi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.StopTime;
import com.example.metroApi.Entity.Trip;
import com.example.metroApi.Entity.Estacion;


@Repository
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {
    List<StopTime> findByTripOrderByStopSequenceAsc(Trip trip);

    List<StopTime> findByEstacionAndArrivalMinutesBetween(
        Estacion estacion, int start, int end
    );
}
