package com.ikadjate.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.PaiementRequestDto;
import com.ikadjate.backend.model.Paiement;
import com.ikadjate.backend.service.PaiementService;

@RestController
@RequestMapping(ApiPaths.PAIEMENT)
public class PaiementController {
	
	 private final PaiementService paiementService;

	    public PaiementController(PaiementService paiementService) {
	        this.paiementService = paiementService;
	    }
	    @PostMapping("/facture/{factureId}")
	    public ResponseEntity<Paiement> ajouterPaiement(@PathVariable Long factureId,
                @RequestBody PaiementRequestDto request) {
                

        Paiement paiement = paiementService.ajouterPaiement(factureId, request);

              return ResponseEntity.ok(paiement);
	          }

     }
