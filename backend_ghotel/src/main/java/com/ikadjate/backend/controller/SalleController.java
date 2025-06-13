package com.ikadjate.backend.controller;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.SalleDto;
import com.ikadjate.backend.model.Salle;
import com.ikadjate.backend.service.SalleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.SALLE)
@RequiredArgsConstructor
 
public class SalleController {

    private final SalleService salleService;

    @GetMapping
    public ResponseEntity<List<Salle>> getAllSalles() {
        return ResponseEntity.ok(salleService.getAllSalles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salle> getSalleById(@PathVariable Long id) {
        return ResponseEntity.ok(salleService.getSalleById(id));
    }

    @PostMapping
    public ResponseEntity<Salle> createSalle(@RequestBody SalleDto salleDto) {
        return ResponseEntity.ok(salleService.createSalle(salleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Salle> updateSalle(@PathVariable Long id, @RequestBody Salle salleDetails) {
        return ResponseEntity.ok(salleService.updateSalle(id, salleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalle(@PathVariable Long id) {
        salleService.deleteSalle(id);
        return ResponseEntity.noContent().build();
    }
}
