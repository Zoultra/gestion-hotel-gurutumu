package com.ikadjate.backend.model;
 
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
public class LigneReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Référence à la réservation principale
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @JsonBackReference
    private Reservation reservation;

    
    
    // Chambre ou salle réservée
    @ManyToOne
    @JoinColumn(name = "chambre_id", nullable = true)
    private Chambre chambre;

    @ManyToOne
    @JoinColumn(name = "salle_id", nullable = true)
    private Salle salle;

    private int nombreJours;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

   
}

