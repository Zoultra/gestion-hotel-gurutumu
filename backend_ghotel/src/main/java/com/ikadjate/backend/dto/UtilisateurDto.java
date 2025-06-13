package com.ikadjate.backend.dto;

 
import lombok.Data;

@Data
public class UtilisateurDto {
    private String nom;
    private String prenom;
    private String username;
    private String telephone;
    private String password;
    private String email;
    private Long role_id;
}