package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ikadjate.backend.model.CategorieDepense;

import lombok.Data;

@Data
public class DepenseDto {
	
	private Long id;
    private String libelle;
    private CategorieDepense categorie;
    private BigDecimal montant;
    private LocalDate dateDepense;
    private String description;

}
