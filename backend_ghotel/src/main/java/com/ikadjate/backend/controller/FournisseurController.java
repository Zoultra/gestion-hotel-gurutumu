package com.ikadjate.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.FournisseurDto;
import com.ikadjate.backend.model.Fournisseur;
import com.ikadjate.backend.service.FournisseurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.FOURNISSEUR)
@CrossOrigin("*")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs();
    }

    @GetMapping("/{id}")
    public Fournisseur getFournisseurById(@PathVariable Long id) {
        return fournisseurService.getFournisseurById(id);
    }

    @PostMapping
    public ResponseEntity<?> createFournisseur(@RequestBody FournisseurDto request) {
        try {
            Fournisseur createdFournisseur = fournisseurService.createFournisseur(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFournisseur);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public Fournisseur updateFournisseur(@PathVariable Long id, @RequestBody Fournisseur fournisseurDetails) {
        return fournisseurService.updateFournisseur(id, fournisseurDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable Long id) {
        fournisseurService.deleteFournisseur(id);
    }
}
