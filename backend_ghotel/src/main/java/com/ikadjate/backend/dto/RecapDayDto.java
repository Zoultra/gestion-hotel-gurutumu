package com.ikadjate.backend.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class RecapDayDto {
    private LocalDate date;
    
    private List<CaisseJourDto> caisses;  // <= Assure-toi que ce champ existe
}
