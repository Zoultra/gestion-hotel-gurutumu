package com.ikadjate.backend.model;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utilisateurs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Utilisateur extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String prenom;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String telephone;
    
    @ManyToOne
    @JoinColumn(name = "role_id" ) 
    private Role role;
    
    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<MouvementCaisse> mouvementCaisse;
    
    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Caisse> caisses;
    
}