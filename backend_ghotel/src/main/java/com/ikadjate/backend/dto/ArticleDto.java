package com.ikadjate.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.LigneVente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
	
	private Long id;
	
    private String designation;
     
    private BigDecimal prixUnitaire;
    
    private String imagePath; 
	    
}
