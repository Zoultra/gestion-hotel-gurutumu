package com.ikadjate.backend.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.ikadjate.backend.model.Client;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CommandeClientDto {
	
	    
	    
	    private Long id;
	    private LocalDate dateCommande;
	    private String etatCommande;
	    @Column(nullable = true)
	    private ClientDto client;
	    private List<LigneCommandeClientDto> lignes;

	    public double getMontantTotal() {
	        return lignes.stream()
	            .mapToDouble(LigneCommandeClientDto::getSousTotal)
	            .sum();
	    }
}
