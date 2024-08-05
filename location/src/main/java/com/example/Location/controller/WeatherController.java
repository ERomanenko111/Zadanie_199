package com.example.location.controller;

import com.example.location.model.Geodata;
import com.example.location.model.Weather;
import com.example.location.repository.GeodataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@RestController
public class WeatherController {
    @Autowired
    private GeodataRepository repository;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/weather")
    public ResponseEntity<Weather> redirectRequestWeather(@RequestParam String location) {
        Optional<Geodata> optionalGeodata = repository.findByName(location);
        if (optionalGeodata.isPresent()) {
            Geodata geodata = optionalGeodata.get();
            String url = String.format("http://localhost:8082/?lat=%s&lon=%s", geodata.getLat(), geodata.getLon());
            return new ResponseEntity(restTemplate.getForObject(url, Weather.class), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Optional<Geodata> getWeather(@RequestParam String location) {
        return repository.findByName(location);
    }

    @PostMapping
    public Geodata save(@RequestBody Geodata geodata) {
        return repository.save(geodata);
    }
}
