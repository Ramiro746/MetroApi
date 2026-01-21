package com.example.metroApi.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.metroApi.Service.EstacionService;

import com.example.metroApi.Dto.EstacionDto;
import com.example.metroApi.Dto.EstimacionLineaDto;
import com.example.metroApi.Dto.HorarioDto;
import com.example.metroApi.Dto.LineaDto;


import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/metro/estaciones")
@RequiredArgsConstructor
public class EstacionController {

    private final EstacionService estacionService;

    @GetMapping
    public List<EstacionDto> listarEstaciones(){
        return estacionService.listarEstaciones();
    }

    @GetMapping("/{nombre}")
    public List <EstacionDto> obtenerEstacion(@PathVariable String nombre){
        return estacionService.obtenerEstacionPorNombre(nombre);
    }
    
    @GetMapping("/{id}/lineas")
    public List<LineaDto> obtenerLineas(@PathVariable Long id) {
        return estacionService.obtenerLineasPorEstacion(id);
    }
    
    @GetMapping("/{id}/lineas/{lineaId}/horarios")
    public List<HorarioDto> obtenerHorarios(@PathVariable Long id,
                                            @PathVariable Long lineaId) {
        return estacionService.obtenerHorarios(id, lineaId);
    }
    
    @GetMapping("/{id}/lineas/{lineaId}/proximo-tren")
    public ResponseEntity<HorarioDto> proximoTren(@PathVariable Long id, @PathVariable Long lineaId) {

        HorarioDto proximo = estacionService.obtenerProximoTren(id, lineaId);

        if(proximo == null){
            return ResponseEntity.noContent().build();//204
        }

        return ResponseEntity.ok(proximo);
    }

    @GetMapping("/{id}/lineas/{lineaId}/estimacion")
    public EstimacionLineaDto estimarProximoTren(@PathVariable Long id, @PathVariable Long lineaId){
        return estacionService.estimarProximoTren(id, lineaId);
    }
}
