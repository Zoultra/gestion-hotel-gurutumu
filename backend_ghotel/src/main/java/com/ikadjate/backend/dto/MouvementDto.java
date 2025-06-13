package com.ikadjate.backend.dto;

 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ikadjate.backend.model.TypeMouvement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MouvementDto {

    private Long id;

    private String description;

    private String type; // ENTREE ou SORTIE

    private BigDecimal montant;

    private LocalDate date;
    
     
}
