package com.ikadjate.backend.dto;


import lombok.Data;

@Data
public class CategorieDto {
	
	private String designation;
    private String description;
	private long entreprise_id;
	private long famille_id;
	
}
