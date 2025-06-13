package com.ikadjate.backend.controller;



import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;

import com.ikadjate.backend.dto.VenteDto;


import com.ikadjate.backend.model.Vente;
import com.ikadjate.backend.service.VenteService;


@RestController
@RequestMapping(ApiPaths.VENTE)
 
public class VenteController {

	
    private final VenteService venteService;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }
    
    
    @PutMapping("/{id}/payer")
    public ResponseEntity<VenteDto> payerVente(@PathVariable Long id) {
        VenteDto updated = venteService.payerVente(id);
        return ResponseEntity.ok(updated);
    }
    

    @PostMapping
    public ResponseEntity<VenteDto> createVente(@RequestBody VenteDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le corps de la commande est vide !");
        }
        return ResponseEntity.ok(venteService.save(dto));
    }
    
    @GetMapping
    public List<Vente> getAllVente() {
        return venteService.getVentes();
    }
    
    @GetMapping("/{id}")
    public Vente VenteById(@PathVariable Long id)  {
        return venteService.getVenteById(id);
    }
    
    @DeleteMapping("/{id}")
   public ResponseEntity<Void> supprimerVente(@PathVariable Long id) {
       venteService.supprimerVente(id);
       return ResponseEntity.noContent().build(); // HTTP 204 No Content
   }
    
    @GetMapping("/last-50-ventes")
    public List<Vente> getLastFiveVentesList() {
        return venteService.getLast50Ventes();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVente(@PathVariable Long id, 
            @RequestBody VenteDto venteDto) { 
    	
    	if (venteDto.getLignesVentes() == null) {
            return ResponseEntity.badRequest().body("Sale lines cannot be null");
        }
 
             VenteDto updatedVente = venteService.updateVente(id, venteDto);
 
              return ResponseEntity.ok(updatedVente);
 
               }
 
    
  
}

