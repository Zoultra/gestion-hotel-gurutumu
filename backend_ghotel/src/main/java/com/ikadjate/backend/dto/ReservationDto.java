package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.ikadjate.backend.model.StatutReservation;

import lombok.Data;

@Data
public class ReservationDto {

    private Long id;
    private String code;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDate dateReservation;
    private Long client_id;
    private ClientDto client;
    private BigDecimal montantTotal;
    private BigDecimal montantRemise;
    private StatutReservation statut;
    private List<LigneReservationDto> lignes;
    
    private FactureDto facture;

    // Getters & Setters
}