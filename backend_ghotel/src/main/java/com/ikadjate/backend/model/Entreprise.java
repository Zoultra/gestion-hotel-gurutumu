package com.ikadjate.backend.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "entreprise")
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nom;
    @Column(nullable = false)
    private String adresse;
    @Column(nullable = false)
    private String telephone;
    @Column(nullable = false, unique = true)
    private String email;
    
     
    
    @OneToMany(mappedBy = "entreprise")
   	@JsonIgnore
    private List<Fournisseur> fournisseur;
    
    @OneToMany(mappedBy = "entreprise")
   	@JsonIgnore
    private List<Categorie> categorie;
    
    
    
    @OneToMany(mappedBy = "entreprise")
   	@JsonIgnore
    private List<LigneCommandeFournisseur> ligneCommandeFournisseur;
     
}
