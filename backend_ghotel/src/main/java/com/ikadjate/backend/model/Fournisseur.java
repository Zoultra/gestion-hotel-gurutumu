package com.ikadjate.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String nom;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String telephone;
    private String observation;
    private String adresse;
    
    
    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;
    
    @OneToMany(mappedBy = "fournisseur")
   	@JsonIgnore
    private List<CommandeFournisseur> commandeFournissseur;
}
