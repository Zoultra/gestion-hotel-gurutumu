package com.ikadjate.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class LigneCommandeClient {
	@Id 
	@GeneratedValue
    private Long id;

    @ManyToOne
    private Article article;

    private int quantite;
    
    private double prixUnitaire;

    public double getSousTotal() {
        return quantite * prixUnitaire;
    }
    
    @ManyToOne
    @JoinColumn(name = "commande_client_id", nullable = false)
    private CommandeClient commandeClient;
}
