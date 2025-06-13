package com.ikadjate.backend.model;

import java.math.BigDecimal;


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
public class LigneCommandeFournisseur {
         
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int qtct; // Qté par carton

    private int quantite;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixAchat;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixRvt;

    @Column(precision = 5, scale = 2)
    private BigDecimal pourcentageMarge;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixVente;

    @Column(precision = 10, scale = 2)
    private BigDecimal montant;
    
    @ManyToOne
    @JoinColumn(name = "commande_fournisseur_id", nullable = false)
    private CommandeFournisseur commandeFournisseur;
    
    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
    
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
