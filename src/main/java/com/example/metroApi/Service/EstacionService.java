package com.example.metroApi.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.metroApi.Repository.EstacionRepository;
import com.example.metroApi.Repository.LineaRepository;
import com.example.metroApi.Repository.EstacionLineaRepository;
import com.example.metroApi.Repository.StopTimeRepository;
import com.example.metroApi.Repository.ServicioCalendarioRepository;
import com.example.metroApi.Repository.TripRepository;


import com.example.metroApi.Entity.ServicioCalendario;
import com.example.metroApi.Entity.Trip;



import jakarta.transaction.Transactional;

import com.example.metroApi.Dto.EstacionDto;
import com.example.metroApi.Dto.HorarioDto;
import com.example.metroApi.Dto.LineaDto;
import com.example.metroApi.Dto.EstimacionLineaDto;
import com.example.metroApi.Dto.EstimacionDireccionDto;


import com.example.metroApi.Entity.Estacion;
import com.example.metroApi.Entity.Linea;


@Service
@Transactional
public class EstacionService {

    private final EstacionRepository estacionRepository;
        
    private final LineaRepository lineaRepository;

    private final EstacionLineaRepository estacionLineaRepository;

    private final StopTimeRepository stopTimeRepository;

    private final ServicioCalendarioRepository servicioCalendarioRepository;
    
    private final TripRepository tripRepository;


    public EstacionService(EstacionRepository estacionRepository, EstacionLineaRepository estacionLineaRepository, StopTimeRepository stopTimeRepository,ServicioCalendarioRepository servicioCalendarioRepository, TripRepository tripRepository, LineaRepository lineaRepository){
        this.estacionRepository = estacionRepository;
        this.estacionLineaRepository = estacionLineaRepository;
        this.stopTimeRepository = stopTimeRepository;
        this.servicioCalendarioRepository = servicioCalendarioRepository;
        this.tripRepository = tripRepository;
        this.lineaRepository = lineaRepository;
    }

    public List<EstacionDto> listarEstaciones(){
        return estacionRepository.findAllDto();
    }

    public List<EstacionDto> obtenerEstacionPorNombre(String nombre){
        List<EstacionDto> estaciones = estacionRepository.findDTOByNombre(nombre);

        if(estaciones.isEmpty()){
            throw new RuntimeException("No existen estaciones con nombre: "+ nombre);
        }

        return estaciones;
    }


    public List<LineaDto> obtenerLineasPorEstacion(Long estacionId) {
        return estacionLineaRepository.findLineasPorEstacion(estacionId);
    }

    public List<HorarioDto> obtenerHorarios(Long estacionId, Long lineaId) {
        return stopTimeRepository.findHorariosPorEstacionYLinea(estacionId, lineaId);
    }

    public HorarioDto obtenerProximoTren(Long estacionId, Long lineaId){

        LocalDate hoy = LocalDate.now();
        DayOfWeek diaSemana = hoy.getDayOfWeek();

        List<ServicioCalendario> servicios =
            servicioCalendarioRepository.findServiciosActivosHoy(hoy, diaSemana.name());
            
        if(servicios.isEmpty()) return null;

        List<Trip> trips = tripRepository.findTripsActivos(lineaId, servicios);

        if(trips.isEmpty()) return null;

        List<HorarioDto> horarios = stopTimeRepository.findHorarios(estacionId, trips);

        LocalTime ahora = LocalTime.of(0, 10); // 00:10
        int ahoraMinutos = ahora.getHour() * 60 + ahora.getMinute();
       
        return horarios.stream()
            .filter(h -> h.getArrivalMinutes() >= ahoraMinutos)
            .min(Comparator.comparing(HorarioDto::getArrivalMinutes))
            .orElse(null);
    }


    public EstimacionLineaDto estimarProximoTren(Long estacionId, Long lineaId){

        Estacion estacion = estacionRepository.findById(estacionId)
            .orElseThrow(() -> new RuntimeException("Estacion no encotrada"));

        Linea linea = lineaRepository.findById(lineaId)
            .orElseThrow(() -> new RuntimeException("Linea no encontrada"));

        LocalTime ahora = LocalTime.now();
        //int frecuencia = calcularFrecuencia(lineaId, ahora);
        
        DayOfWeek dia = LocalDate.now().getDayOfWeek();
        //Simulamos espera
        int ahoraMin = ahora.getHour() * 60 + ahora.getMinute();
        //int minutosHastaProximo = frecuencia - (ahoraMin % frecuencia);

        List<EstimacionDireccionDto> estimaciones = new ArrayList<>();

        for(int direccionId = 0; direccionId <=1; direccionId++){
            int frecuencia = calcularFrecuencia(lineaId, ahora, direccionId, dia);
            int minutosHastaProximo = frecuencia - (ahoraMin % frecuencia);

            //if(minutosHastaProximo == frecuencia) minutosHastaProximo = 0; SI DEJO ESTO LUEGO DIRA QUE EL TREN ESTA LLEGANDO AHORA MISMO osea 0 minutos

            if(minutosHastaProximo == 0){
                minutosHastaProximo = frecuencia;
            } //Si justo acaba de pasar un tren el siguiente llegara en "frecuencia" minutos no en 0, osea en el tiempo que nos diga la frecuencia. 

            estimaciones.add(
                EstimacionDireccionDto.builder()
                    .direccionId(direccionId)
                    .direccionNombre(obtenerNombreDireccion(direccionId, linea))
                    .frecuenciaMin(frecuencia)
                    .proximoTrenMin(minutosHastaProximo)
                    .build()
            );
        }

        return EstimacionLineaDto.builder()
            .estacionId(estacion.getId())
            .estacionNombre(estacion.getNombre())
            .lineaId(linea.getId())
            .lineaNombre(linea.getNombre())
            .horaConsulta(ahora.format(DateTimeFormatter.ofPattern("HH:mm"))) // <-- añadimos la hora
            .estimado(true)
            .direcciones(estimaciones)
            .build();
    }

    private String obtenerNombreDireccion(int directionId, Linea linea) {
        return directionId == 0 ? "Sentido A" : "Sentido B";
    }

    private int calcularFrecuencia(Long lineaId, LocalTime hora, int direccionId, DayOfWeek dia){

        boolean finde =
            dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;

        int h = hora.getHour();

        if (!finde){
            if(h >= 7 && h <= 10) return 3; //hora punta mañana
            if(h >= 16 && h <= 20) return 3; //hora punta tarde
            if(h >= 6 && h <= 23) return 4; //hora normal
        }else{
            if(h >= 8 && h <= 22) return 6;
        }
        
        return 10; //noche
    }
}
