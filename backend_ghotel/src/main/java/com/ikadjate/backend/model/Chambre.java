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
public class Chambre {
	

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(nullable = false, unique = true)
	    private String numero;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private TypeChambre type;
	    @Column(nullable = false)
	    private BigDecimal tarifParNuit;
	    
	    private String description;

	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private EtatChambre etat;
	    
	    
	    @OneToMany(mappedBy = "chambre", fetch = FetchType.LAZY)
	   	@JsonIgnore
	    private List<LigneReservation> ligneReservation;
 
	 

}
