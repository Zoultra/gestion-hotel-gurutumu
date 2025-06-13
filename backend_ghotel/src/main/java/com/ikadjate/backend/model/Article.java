package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Article extends BaseEntity{
     
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true, nullable = false)
    private String designation;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;
     
    @OneToMany(mappedBy = "article")
   	@JsonIgnore
    private List<LigneCommandeFournisseur> ligneCommandeFournisseur;
    
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
   	@JsonIgnore
    private List<LigneVente> ligneVente;
    
    
    private String imagePath;
    
    

    
}
