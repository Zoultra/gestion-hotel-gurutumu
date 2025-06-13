package com.ikadjate.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikadjate.backend.config.BaseEntity;

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
public class Client extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Column(nullable = true, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String telephone;
    private String observation;
    private String adresse;
    
    @OneToMany(mappedBy = "client")
   	@JsonIgnore
    private List<CommandeClient> commandeClient;
    
}