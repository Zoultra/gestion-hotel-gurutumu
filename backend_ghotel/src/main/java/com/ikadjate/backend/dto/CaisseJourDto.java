package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ikadjate.backend.model.Utilisateur;

 
import lombok.Builder;
import lombok.Data;

@Data
 
@Builder
public class CaisseJourDto {
	
	     private LocalDate date;
	     private Long caisseId;
	     private LocalDate date_ouverture;
	     private LocalDate date_fermeture;
	    private Utilisateur utilisateur;
	    private BigDecimal soldeInitial;
	    private BigDecimal totalEntree;
	    private BigDecimal totalSortie;
	    private BigDecimal soldeFinal;
	    private BigDecimal soldeFinalTheorique;
	    private LocalTime heureOuverture;
	    private LocalTime heureFermeture;
	    private Boolean ouverte;
	    private List<MouvementDto> mouvements;
	    
	   
}
