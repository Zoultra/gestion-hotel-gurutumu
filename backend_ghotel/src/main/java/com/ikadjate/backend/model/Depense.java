package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Depense extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable= false)
    private String libelle;
    @Column(nullable= false)
    @Enumerated(EnumType.STRING)
    private CategorieDepense categorie;
    @Column(nullable= false)
    private BigDecimal montant;
    @Column(nullable= false)
    private LocalDate dateDepense;
 
    private String description;

}
