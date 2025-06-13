package com.ikadjate.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.FamilleDto;
import com.ikadjate.backend.model.Famille;
import com.ikadjate.backend.service.FamilleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.FAMILLE)
@RequiredArgsConstructor
public class FamilleController {

    private final FamilleService familleService;

    @GetMapping
    public List<Famille> getAllFamilles() {
        return familleService.getAllFamilles();
    }

    @GetMapping("/{id}")
    public Famille getFamilleById(@PathVariable Long id) {
        return familleService.getFamilleById(id);
    }

    @PostMapping
    public ResponseEntity<?> createFamille(@RequestBody FamilleDto request) {
        try {
            Famille createdFamille = familleService.createFamille(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFamille);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public Famille updateFamille(@PathVariable Long id, @RequestBody Famille familleDetails) {
        return familleService.updateFamille(id, familleDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteFamille(@PathVariable Long id) {
        familleService.deleteFamille(id);
    }
}
