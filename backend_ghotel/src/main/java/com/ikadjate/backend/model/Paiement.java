package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Paiement{
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(nullable = false)
	    private BigDecimal montant; // Montant payé
	    @Column(nullable = false)
	    private LocalDate datePaiement; // Date du paiement

	    @Enumerated(EnumType.STRING)
	    private ModePaiement modePaiement; // Ex: ESPECES, CARTE, MOBILE

	    @ManyToOne(optional = false)
	    @JoinColumn(name = "facture_id", nullable = false)
	    @JsonIgnore
	    private Facture facture;

	    private String commentaire; // Optionnel : note ou détail sur le paiement


}
