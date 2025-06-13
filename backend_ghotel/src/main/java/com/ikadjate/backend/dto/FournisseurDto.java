 package com.ikadjate.backend.dto;


import lombok.Data;

@Data
public class FournisseurDto {
     
	    private String nom;
	    private String email;
	    private String telephone;
	    private String observation;
	    private String adresse;
	    private long entreprise_id;
}

