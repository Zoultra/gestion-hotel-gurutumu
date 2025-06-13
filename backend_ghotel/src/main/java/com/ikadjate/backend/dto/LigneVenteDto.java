package com.ikadjate.backend.dto;

 

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LigneVenteDto {
	    private Long id;
	    private Long article_id;
	    private int quantite;
	    private BigDecimal prixUnitaire;
	    private Long vente_id;
	    
	    
	    public BigDecimal getSousTotal() {
	        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
	    }

}
