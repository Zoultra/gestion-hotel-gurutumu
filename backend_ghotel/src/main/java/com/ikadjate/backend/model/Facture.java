package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Facture {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String numero; // Exemple : FACT-20240601-001

	    @Column(nullable = false)
	    private LocalDate dateFacture;

	    @Column(nullable = false)
	    private BigDecimal montantTotal;

	    @Column(nullable = false)
	    private BigDecimal montantRemise = BigDecimal.ZERO;

	    @Column(nullable = false)
	    private BigDecimal montantNet; // = montantTotal - remise

	    @Column(nullable = false)
	    private BigDecimal montantPaye = BigDecimal.ZERO;

	    @Column(nullable = false)
	    private BigDecimal resteAPayer;

	    @Enumerated(EnumType.STRING)
	    private StatutFacture statut = StatutFacture.EN_ATTENTE;
	    
	    @ManyToOne(optional = false)
	    @JoinColumn(name = "client_id")
	    private Client client;

	    // Une facture concerne une seule réservation
	    @OneToOne
	    @JoinColumn(name = "reservation_id", nullable = false)
	    @JsonManagedReference
	    private Reservation reservation;

	    // Paiements liés à cette facture
	    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Paiement> paiements;
	    
	   

}
