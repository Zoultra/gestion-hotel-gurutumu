 package com.ikadjate.backend.dto;

import java.math.BigDecimal;


import lombok.Data;


@Data
public class LigneCommandeFournisseurDto {

    private Long id;
    private int qtct;
    private int quantite;
    private BigDecimal prixAchat;
    private BigDecimal prixRvt;
    private BigDecimal pourcentageMarge;
    private BigDecimal prixVente;
    private BigDecimal montant;

    private Long commandeFournisseur_id;
  //  private Long entreprise_id;
    private Long article_id;
}
