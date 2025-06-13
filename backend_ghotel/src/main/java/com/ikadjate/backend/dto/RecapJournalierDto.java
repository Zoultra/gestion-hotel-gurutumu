 package com.ikadjate.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecapJournalierDto {

    private LocalDate dateOuverture;
    
    private LocalDate dateFermeture;
    
    private LocalTime heureOuverture;
    
    private LocalTime heureFermeture;
    
    private BigDecimal soldeDepart;

    private BigDecimal totalEntree;

    private BigDecimal totalSortie;

    private BigDecimal soldeFinal;
    
    private BigDecimal soldeFinalTheorique;

    private List<MouvementDto> mouvements;
}
