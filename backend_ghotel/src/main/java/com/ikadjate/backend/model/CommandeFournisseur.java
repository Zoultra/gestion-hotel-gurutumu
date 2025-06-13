package com.ikadjate.backend.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class CommandeFournisseur extends BaseEntity {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @Column(nullable = false, unique = true)
	    private String code;
	    @Column(nullable = false)
	    private Date dateCommande;
	    @Column(nullable = false)
	    private String etatCommande;
	     
	    @ManyToOne
	    @JoinColumn(name = "fournisseur_id")
	    private Fournisseur fournisseur;
	    
	    @ManyToOne
	    @JoinColumn(name = "entreprise_id")
	    private Entreprise entreprise;

}
