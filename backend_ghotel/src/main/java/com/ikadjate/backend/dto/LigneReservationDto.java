package com.ikadjate.backend.dto;


import java.math.BigDecimal;
import java.util.List;

import com.ikadjate.backend.model.Chambre;
import com.ikadjate.backend.model.Salle;

import lombok.Data;

@Data
public class LigneReservationDto {

    private Long id;
    private Long chambreId; // ou salleId
    private int nombreJours;
    private BigDecimal prixUnitaire;
    private Long idObjet; // identifiant soit de chambre, soit de salle
    private String type; // "CHAMBRE" ou "SALLE"
     
    
   // public BigDecimal getSousTotal() {
    //    return prixUnitaire.multiply(BigDecimal.valueOf(nombreJours));
   // }
    
    private Chambre chambre; // pour l'objet chambre complet
    private Salle salle;     // pour l'objet salle complet
    
    
}