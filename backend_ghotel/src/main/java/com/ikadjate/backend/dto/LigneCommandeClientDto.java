package com.ikadjate.backend.dto;

import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.CommandeClient;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class LigneCommandeClientDto {
	 
	    
	    
	    
	    private Long articleId;
	    private int quantite;
	    private double prixUnitaire;

	    public double getSousTotal() {
	        return quantite * prixUnitaire;
	    }
}
