package com.ikadjate.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ikadjate.backend.config.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor 
@AllArgsConstructor
public class Vente  extends BaseEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String code;
    
    @Column(nullable = false)
    private LocalDate dateVente;
    
    @Column(nullable = false)
    private BigDecimal montantTotal;
    
    @Column(nullable = false)
    private BigDecimal montantPaye;
    
    @Column(nullable = false)
    private BigDecimal monnaie;
    
    @Column(nullable = false)
    private String tableResto;
    
    @Enumerated(EnumType.STRING)
    private EtatVente etatVente;
    
  ///  @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
  //  @JsonIgnore
  //  private List<MouvementCaisse> mouvements;
    
    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LigneVente> ligneVentes;
    
    
    
	

}
