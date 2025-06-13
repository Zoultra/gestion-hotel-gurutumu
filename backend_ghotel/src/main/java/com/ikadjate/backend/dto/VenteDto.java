package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ikadjate.backend.model.EtatVente;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class VenteDto {
	  
	    private Long id;
	    private LocalDate dateVente;
	    private EtatVente etatVente;
	    private String code;
	    private List<LigneVenteDto> lignesVentes = new ArrayList<>();
	    private BigDecimal montantTotal;
	    private BigDecimal montantPaye;
	    private BigDecimal monnaie;
	    private String tableResto;
	    
	    public List<LigneVenteDto> getLignes() {
	        return lignesVentes;
	    }

	    // Setter
	    public void setLignes(List<LigneVenteDto> lignesVentes) {
	        this.lignesVentes = lignesVentes != null ? lignesVentes : new ArrayList<>();
	    }
	    
}
