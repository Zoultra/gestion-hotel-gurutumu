package com.ikadjate.backend.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Data
public class CommandeFournisseurDto {
          
	    private String code;
	    
	    private Date dateCommande;
	    
	    private String etatCommande;
	     
	    private long fournisseur_id;
	    
	    private long entreprise_id;
}
