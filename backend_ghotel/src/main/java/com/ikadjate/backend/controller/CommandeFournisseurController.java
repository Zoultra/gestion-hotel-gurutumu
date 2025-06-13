// CommandeFournisseurController.java
package com.ikadjate.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.CommandeFournisseurDto;
import com.ikadjate.backend.dto.LigneCommandeFournisseurDto;
import com.ikadjate.backend.model.CommandeFournisseur;
import com.ikadjate.backend.service.CommandeFournisseurService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.CMDTFOURNISSEUR)
@RequiredArgsConstructor
public class CommandeFournisseurController {

    private final CommandeFournisseurService commandeService;

    @PostMapping
    public ResponseEntity<?> createCommandeFournisseur(
            @RequestBody Map<String, Object> body) {

        try {
            CommandeFournisseurDto commandeDto = 
                new com.fasterxml.jackson.databind.ObjectMapper().convertValue(body.get("commande"), CommandeFournisseurDto.class);

            List<LigneCommandeFournisseurDto> lignes = 
                new com.fasterxml.jackson.databind.ObjectMapper().convertValue(body.get("lignes"),
                        new com.fasterxml.jackson.core.type.TypeReference<List<LigneCommandeFournisseurDto>>() {});

            CommandeFournisseur saved = commandeService.createCommandeFournisseur(commandeDto, lignes);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
