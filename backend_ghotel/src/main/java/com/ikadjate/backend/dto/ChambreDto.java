package com.ikadjate.backend.dto;

import java.math.BigDecimal;
 
import com.ikadjate.backend.model.EtatChambre;
 
import com.ikadjate.backend.model.TypeChambre;

 
import lombok.Data;

@Data
public class ChambreDto {
	
	private String numero;
    private TypeChambre type;
    private BigDecimal tarifParNuit;
    private EtatChambre etat;
    private String description;
    
    

}
