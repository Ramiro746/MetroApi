package com.example.metroApi.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.metroApi.Entity.Trip;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long>{

    Optional<Trip> findByGtfsTripId(String gtfsTripId);

    boolean existsByGtfsTripId(String gtfsTripId);

}
