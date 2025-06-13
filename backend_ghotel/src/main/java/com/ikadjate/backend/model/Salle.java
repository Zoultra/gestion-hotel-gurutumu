package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salle {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(nullable = true, unique = true)
	    private String numero;
	 //   @Column(unique = true)
	 //   private String nom;
	    @Column(nullable = false)
	    private int capaciteMax;
	    @Column(nullable = false)
	    private String equipements;
	    @Enumerated(EnumType.STRING)
	    private TypeSalle type;
	    @Enumerated(EnumType.STRING)
	    private StatutSalle statut;
	    @Column(nullable = false)
	    private BigDecimal tarif;
	    
	    @OneToMany(mappedBy = "salle", fetch = FetchType.LAZY)
	   	@JsonIgnore
	    private List<LigneReservation> ligneReservation;
       
}
