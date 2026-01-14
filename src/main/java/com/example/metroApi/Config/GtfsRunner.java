package com.example.metroApi.Config;

import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import com.example.metroApi.Service.GtfsImportService;

@Component
public class GtfsRunner implements CommandLineRunner {
    private final GtfsImportService service;

    public GtfsRunner(GtfsImportService service){
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.importGtfs();
        service.importarEstacionesLineas();//hace la query, creo xd
    }
}
