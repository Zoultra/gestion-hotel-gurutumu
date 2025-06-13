package com.ikadjate.backend.dto;

import java.math.BigDecimal;

import com.ikadjate.backend.model.StatutSalle;
import com.ikadjate.backend.model.TypeSalle;

 
import lombok.Data;

@Data
public class SalleDto {
	 
	    private String numero;
	  
	 //   private String nom;
	   
	    private int capaciteMax;
	    
	    private String equipements;
	   
	    private TypeSalle type;
	    
	    private StatutSalle statut;
	    
	    private BigDecimal tarif;
}
