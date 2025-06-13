package com.ikadjate.backend.controller;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.ChambreDto;
import com.ikadjate.backend.model.Chambre;
import com.ikadjate.backend.service.ChambreService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.CHAMBRE)
@RequiredArgsConstructor
public class ChambreController {

    private final ChambreService chambreService;

    @GetMapping
    public ResponseEntity<List<Chambre>> getAllChambres() {
        return ResponseEntity.ok(chambreService.getAllChambres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Long id) {
        return ResponseEntity.ok(chambreService.getChambreById(id));
    }

    @PostMapping
    public ResponseEntity<Chambre> createChambre(@RequestBody ChambreDto chambreDto) {
        return ResponseEntity.ok(chambreService.createChambre(chambreDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chambre> updateChambre(@PathVariable Long id, @RequestBody Chambre chambreDetails) {
        return ResponseEntity.ok(chambreService.updateChambre(id, chambreDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChambre(@PathVariable Long id) {
        chambreService.deleteChambre(id);
        return ResponseEntity.noContent().build();
    }
}
