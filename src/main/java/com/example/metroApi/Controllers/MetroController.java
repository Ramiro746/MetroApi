package com.example.metroApi.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MetroController {
    @GetMapping("/metro/estaciones/{id}/")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
