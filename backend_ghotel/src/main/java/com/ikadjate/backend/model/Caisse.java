package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Caisse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate  dateOuverture;

    private LocalDate  dateFermeture;

    private BigDecimal soldeInitial;

    private BigDecimal soldeFinal;
    
    private BigDecimal soldeFinalTheorique;

    private Boolean ouverte;
    
    private LocalTime heureOuverture;
    private LocalTime heureFermeture;
    
    
    @PrePersist
    public void onCreate() {
        this.heureOuverture = LocalTime.now();
    }

    public void fermer() {
        this.ouverte = false;
        this.heureFermeture = LocalTime.now();
    }

    
     
    @ManyToOne
    private Utilisateur utilisateur; // Optionnel : qui a ouvert/fermé

    @OneToMany(mappedBy = "caisse")
    @JsonManagedReference
    private List<MouvementCaisse> mouvements;
}

