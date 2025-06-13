package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ikadjate.backend.model.Paiement;
import com.ikadjate.backend.model.StatutFacture;

import lombok.Data;

@Data
public class FactureDto {
	
	private Long id;
    private String numero;
    private BigDecimal montantTotal;
    private BigDecimal montantNet;
    private BigDecimal montantPaye;
    private BigDecimal resteAPayer;
    private BigDecimal montantRemise;
    private StatutFacture statut;
    private LocalDate dateFacture;
    
    private List<Paiement> paiements;

}
