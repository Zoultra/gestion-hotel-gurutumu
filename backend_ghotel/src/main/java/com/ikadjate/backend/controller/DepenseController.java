package com.ikadjate.backend.controller;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.DepenseDto;
import com.ikadjate.backend.model.Depense;
import com.ikadjate.backend.service.DepenseService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiPaths.DEPENSE)
@RequiredArgsConstructor
public class DepenseController {

    private final DepenseService depenseService;

    @GetMapping
    public ResponseEntity<List<Depense>> getAllDepensesDesc() {
        return ResponseEntity.ok(depenseService.getAllDepensesDesc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Depense> getDepenseById(@PathVariable Long id) {
        return ResponseEntity.ok(depenseService.getDepenseById(id));
    }

    @PostMapping
    public ResponseEntity<Depense> createDepense(@RequestBody DepenseDto depenseDto) {
        return ResponseEntity.ok(depenseService.createDepense(depenseDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Depense> updateDepense(@PathVariable Long id, @RequestBody DepenseDto depenseDto) {
        return ResponseEntity.ok(depenseService.updateDepense(id, depenseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepense(@PathVariable Long id) {
        depenseService.deleteDepense(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/list-depenses-montant-interval")
    public ResponseEntity<Map<String, Object>> getDepensesInterval(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        Map<String, Object> result = depenseService.getDepensesEtMontantEntre(start, end);
        return ResponseEntity.ok(result);
    }
}
