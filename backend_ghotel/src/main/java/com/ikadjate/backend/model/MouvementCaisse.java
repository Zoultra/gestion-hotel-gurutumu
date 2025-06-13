package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementCaisse extends BaseEntity{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private TypeMouvement type; // "ENTREE" ou "SORTIE"

    private BigDecimal montant;

    private String description;
    
    
    // Clés étrangères optionnelles selon le type de mouvement
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "vente_id")
    private Long venteId;

    @Column(name = "depense_id")
    private Long depenseId;
    
    @Column(name = "paiement_id")
    private Long paiementId;
    
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    
    
    

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    
    @ManyToOne
    @JoinColumn(name = "caisse_id", nullable = false)
    @JsonBackReference
    private Caisse caisse;

}
