package com.ikadjate.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.CaisseJourDto;
import com.ikadjate.backend.dto.ChambreDto;
import com.ikadjate.backend.dto.RecapDayDto;
import com.ikadjate.backend.dto.RecapJournalierDto;
import com.ikadjate.backend.model.Caisse;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.service.CaisseService;

@RestController
@RequestMapping(ApiPaths.CAISSE)
public class CaisseController {
	
	    @Autowired
	    private CaisseService caisseService;

	   

	    @PostMapping
	    public ResponseEntity<MouvementCaisse> addMouvement(@RequestBody MouvementCaisse mouvement) {
	        return ResponseEntity.ok(caisseService.enregistrerMouvement(mouvement));
	    }

	    @GetMapping
	    public ResponseEntity<List<MouvementCaisse>> allMouvements() {
	        return ResponseEntity.ok(caisseService.getAllMouvements());
	    }
	    


	    @GetMapping("/operations-journaliere")
	    public ResponseEntity<RecapJournalierDto> getOperationsJournaliere(
	            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

	    	RecapJournalierDto recap = caisseService.getOperationsJournalierEParDate(date);
	        return ResponseEntity.ok(recap);
	    }
	    
	    
	    @GetMapping("/operations-journaliere-interval")
	    public ResponseEntity<List<RecapJournalierDto>> getRecapitulatifEntreDeuxDates(
	            @RequestParam("startDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
	            @RequestParam("endDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {

	        List<RecapJournalierDto> recap = caisseService.getOperationsEntreDatesIn0703(startDateTime, endDateTime);
	        return ResponseEntity.ok(recap);
	    }
	   
	    
	    @PostMapping("/ouverture")
	    public ResponseEntity<Caisse> ouvrirCaisse(@RequestBody Caisse caissedata) {
	        Caisse caisse = caisseService.ouvrirCaisse(caissedata.getSoldeInitial());
	        return ResponseEntity.ok(caisse);
	    }
	    
	    @PostMapping("/fermeture/{id}")
	    public ResponseEntity<Caisse> fermerCaisseById(@PathVariable Long id, @RequestBody Caisse caisseRequest) {
	        
	        return  ResponseEntity.ok(caisseService.fermerCaisse(id,caisseRequest));
	    }
	    
	    @GetMapping("/toutes-caisses")
	    public ResponseEntity<List<Caisse>> getListAllCaisses() {
	        return ResponseEntity.ok(caisseService.getAllCaisses());
	    }
	    
	    
	  
	    
	    
	    @GetMapping("/caisse-du-jour1")
	    public ResponseEntity <List<Caisse>> caisseDuJour() {
	       
	        return ResponseEntity.ok(caisseService.getAllCaissesDujour());
	    }
	    
	    @GetMapping("/caisse-du-jour")
	    public List<Caisse> getCaissesDuJour() {
	        return caisseService.getAllCaissesDujour();
	    }


}
