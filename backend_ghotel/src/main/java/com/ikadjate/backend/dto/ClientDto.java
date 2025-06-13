package com.ikadjate.backend.dto;

 
import lombok.Data;

@Data
public class ClientDto {
        
	    private Long id;
	    private String nom;
	    private String prenom;
	    private String email;
	    private String telephone;
	    private String observation;
	    private String adresse;
	  
}
