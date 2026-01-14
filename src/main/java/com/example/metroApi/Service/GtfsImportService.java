package com.example.metroApi.Service;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.input.BOMInputStream;

import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import com.example.metroApi.Entity.Estacion;
import com.example.metroApi.Entity.Trip;
import com.example.metroApi.Entity.Frecuencia;
import com.example.metroApi.Entity.StopTime;

import com.example.metroApi.Repository.*;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;

import com.example.metroApi.Entity.Linea;
import com.example.metroApi.Entity.ServicioCalendario;

@Service
@Transactional
public class GtfsImportService {

    private final StopTimeRepository stopTimeRepository;

    private final LineaRepository lineaRepository;

    private final EstacionRepository estacionRepository;

    private final FrecuenciaRepository frecuenciaRepository;

    private final ServicioCalendarioRepository servicioCalendarioRepository;

    private final TripRepository tripRepository;

    private final EstacionLineaRepository estacionLineaRepository;

    private int parseGtfsTimeInMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        return hours * 60 + minutes; // ignoramos segundos para simplificar
    }

    @Value("${gtfs.path}")
    private String gtfsPath;

    public GtfsImportService(EstacionRepository estacionRepository, LineaRepository lineaRepository, FrecuenciaRepository frecuenciaRepository, 
                            ServicioCalendarioRepository servicioCalendarioRepository, TripRepository tripRepository, StopTimeRepository stopTimeRepository,
                            EstacionLineaRepository estacionLineaRepository) {
        this.estacionRepository = estacionRepository;
        this.lineaRepository = lineaRepository;
        this.frecuenciaRepository = frecuenciaRepository;
        this.servicioCalendarioRepository = servicioCalendarioRepository;
        this.tripRepository = tripRepository;
        this.stopTimeRepository = stopTimeRepository;
        this.estacionLineaRepository = estacionLineaRepository;
    }

    public void importarEstacionesLineas() {
        estacionLineaRepository.poblarEstacionLinea();
    }

    public void importGtfs(){
        try (ZipFile zipFile = new ZipFile(gtfsPath)){
            
            ZipEntry stopsEntry = zipFile.getEntry("stops.txt");
            if(stopsEntry == null) {
                throw new RuntimeException("Archivo stops.txt no encontrado en el GTFS");
            }

            ZipEntry routesEntry = zipFile.getEntry("routes.txt");
            if(routesEntry == null) {
                throw new RuntimeException("Archivo routes.txt no encontrado en el GTFS");
            }

            ZipEntry calendarEntry = zipFile.getEntry("calendar.txt");
            if(calendarEntry == null) {
                throw new RuntimeException("Archivo frequencies.txt no encontrado en el GTFS");
            }

            ZipEntry frecuencyEntry = zipFile.getEntry("frequencies.txt");
            if(frecuencyEntry == null) {
                throw new RuntimeException("Archivo frequencies.txt no encontrado en el GTFS");
            }

            ZipEntry tripsEntry = zipFile.getEntry("trips.txt");
            if(tripsEntry == null) {
                throw new RuntimeException("Archivo trips.txt no encontrado en el GTFS");
            }

            ZipEntry stopTimesEntry = zipFile.getEntry("stop_times.txt");
            if(stopTimesEntry == null) {
                throw new RuntimeException("Archivo stop_times.txt no encontrado en el GTFS");
            }


            //estaciones
            try(InputStream is = zipFile.getInputStream(stopsEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {

                        String gtfsId = record.get("stop_id");

                        if (!estacionRepository.existsByGtfsId(gtfsId)) {

                            Estacion estacion = new Estacion();
                            estacion.setGtfsId(gtfsId);
                            estacion.setNombre(record.get("stop_name"));
                            estacion.setLatitud(new BigDecimal(record.get("stop_lat")));
                            estacion.setLongitud(new BigDecimal(record.get("stop_lon")));

                            estacionRepository.save(estacion);
                        }
                    }
                }
            
            //Lineas
            try(InputStream is = zipFile.getInputStream(routesEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {
                        
                        String routeId = record.get("route_id");
                        String nombreLinea = record.get("route_long_name");

                        if (!lineaRepository.existsByNombre(nombreLinea)) {

                            Linea linea = new Linea();
                            linea.setRouteId(routeId);
                            linea.setNombre(nombreLinea);
                            linea.setActivo(true);

                            lineaRepository.save(linea);
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException("Error al importar Lineas", e);
                }
            

            //Tipo de dia
            try(InputStream is = zipFile.getInputStream(calendarEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {

                        String serviceId = record.get("service_id");
                        
                        if(servicioCalendarioRepository.existsByGtfsServiceId(serviceId)) continue;
                        
                        ServicioCalendario servicio = new ServicioCalendario();
                        servicio.setGtfsServiceId(serviceId);

                        servicio.setMonday(record.get("monday").equals("1"));
                        servicio.setTuesday(record.get("tuesday").equals("1"));
                        servicio.setWednesday(record.get("wednesday").equals("1"));
                        servicio.setThursday(record.get("thursday").equals("1"));
                        servicio.setFriday(record.get("friday").equals("1"));
                        servicio.setSaturday(record.get("saturday").equals("1"));
                        servicio.setSunday(record.get("sunday").equals("1"));

                        servicio.setStartDate(LocalDate.parse(record.get("start_date"),
                            DateTimeFormatter.BASIC_ISO_DATE));
                        servicio.setEndDate(LocalDate.parse(record.get("end_date"),
                            DateTimeFormatter.BASIC_ISO_DATE));

                        servicioCalendarioRepository.save(servicio);
                    }   

                } catch (Exception e) {
                    throw new RuntimeException("Error al importar frecuencia de linea", e);
                }

            //Trips (necesario para que fuuncione frecuencias)
            //Map<String, Linea> tripIdToLinea = new HashMap<>();

            try(InputStream is = zipFile.getInputStream(tripsEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {

                        String tripId = record.get("trip_id");
                        String routeId = record.get("route_id");
                        String serviceId = record.get("service_id");

                        if (tripRepository.existsByGtfsTripId(tripId)) {
                            continue;
                        }

                        Linea linea = lineaRepository.findByRouteId(routeId)
                            .orElseThrow(() -> new RuntimeException("Linea no encontrada para route_id=" + routeId));

                        ServicioCalendario servicio = servicioCalendarioRepository.findByGtfsServiceId(serviceId)
                            .orElseThrow(() ->  
                                    new RuntimeException("Servicio no encontrado para service_id = " + serviceId));
                        
                       Trip trip = new Trip();
                       trip.setGtfsTripId(tripId);
                       trip.setLinea(linea);
                       trip.setServicioCalendario(servicio);
                       trip.setDirectionId(Integer.parseInt(record.get("direction_id")));

                       tripRepository.save(trip);
                    }   

                } catch (Exception e) {
                    throw new RuntimeException("Error al importar trips", e);
                }


            //Frecuencia (se relaciona con linea)
            try(InputStream is = zipFile.getInputStream(frecuencyEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {

                        String tripId = record.get("trip_id");
                        

                        Trip trip = tripRepository.findByGtfsTripId(tripId)
                            .orElseThrow(() -> new RuntimeException("Trip no encontrado: " + tripId));

                        ServicioCalendario servicio = trip.getServicioCalendario();

                        Frecuencia frecuencia = new Frecuencia();
                        frecuencia.setTrip(trip);
                        frecuencia.setServicio(servicio);
                        frecuencia.setStartTime(parseGtfsTimeInMinutes(record.get("start_time")));
                        frecuencia.setEndtTime(parseGtfsTimeInMinutes(record.get("end_time")));
                        frecuencia.setMinutos(Integer.parseInt(record.get("headway_secs")) / 60);

                        frecuenciaRepository.save(frecuencia);
                    }   

                } catch (Exception e) {
                    throw new RuntimeException("Error al importar frecuencia de linea", e);
                }
            
            //StopTimes
            try(InputStream is = zipFile.getInputStream(stopTimesEntry);
                BOMInputStream bom = new BOMInputStream(is);
                Reader reader = new InputStreamReader(bom, StandardCharsets.UTF_8)) {

                    CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreSurroundingSpaces()
                        .withIgnoreEmptyLines()
                        .parse(reader);

                    for (CSVRecord record : parser) {

                        String tripId = record.get("trip_id");
                        String stopId = record.get("stop_id");

                        Trip trip = tripRepository.findByGtfsTripId(tripId)
                            .orElseThrow(() -> new RuntimeException("Trip no encontrado: " + tripId));

                        Estacion estacion = estacionRepository.findByGtfsId(stopId)
                            .orElseThrow(() -> new RuntimeException("Estacion no encontrada para: "+ stopId));
                        
                        StopTime stopTime = new StopTime();
                        stopTime.setTrip(trip);
                        stopTime.setEstacion(estacion);
                        stopTime.setArrivalMinutes(parseGtfsTimeInMinutes(record.get("arrival_time")));
                        stopTime.setDepartureMinutes(parseGtfsTimeInMinutes(record.get("departure_time")));
                        stopTime.setStopSequence(Integer.parseInt(record.get("stop_sequence")));

                        stopTimeRepository.save(stopTime);
                    }   

                } catch (Exception e) {
                    throw new RuntimeException("Error al importar stop_times", e);
                }


            

        } catch (Exception e) {
            throw new RuntimeException("Error al importar GTFS", e);
        }

    }

}
