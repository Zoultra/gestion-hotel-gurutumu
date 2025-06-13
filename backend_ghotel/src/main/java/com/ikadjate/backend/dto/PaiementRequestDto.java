package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ikadjate.backend.model.ModePaiement;

import lombok.Data;

@Data
public class PaiementRequestDto {
    private BigDecimal montant;
    private ModePaiement modePaiement;
    private String commentaire;
    private LocalDate datePaiement; // Facultatif
}
