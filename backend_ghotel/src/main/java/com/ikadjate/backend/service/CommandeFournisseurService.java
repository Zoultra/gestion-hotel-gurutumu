// CommandeFournisseurService.java
package com.ikadjate.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.CommandeFournisseurDto;
import com.ikadjate.backend.dto.LigneCommandeFournisseurDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.CommandeFournisseur;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.model.Fournisseur;
import com.ikadjate.backend.model.LigneCommandeFournisseur;
import com.ikadjate.backend.repository.ArticleRepository;
import com.ikadjate.backend.repository.CommandeFournisseurRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;
import com.ikadjate.backend.repository.FournisseurRepository;
import com.ikadjate.backend.repository.LigneCommandeFournisseurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeFournisseurService {

    private final CommandeFournisseurRepository commandeRepo;
    private final LigneCommandeFournisseurRepository ligneRepo;
    private final FournisseurRepository fournisseurRepo;
    private final EntrepriseRepository entrepriseRepo;
    private final ArticleRepository articleRepo;

     
    
    public CommandeFournisseur createCommandeFournisseur (CommandeFournisseurDto dto, List<LigneCommandeFournisseurDto> lignesDto) {

        // 1. Récupération de l'entreprise
        Entreprise entreprise = entrepriseRepo.findById(dto.getEntreprise_id())
            .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        // 2. Récupération du fournisseur
        Fournisseur fournisseur = fournisseurRepo.findById(dto.getFournisseur_id())
            .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        // 3. Création de la commande
        CommandeFournisseur commande = new CommandeFournisseur();
        commande.setCode(dto.getCode());
        commande.setDateCommande(dto.getDateCommande());
        commande.setEtatCommande(dto.getEtatCommande());
        commande.setFournisseur(fournisseur);
        commande.setEntreprise(entreprise);
        
        // 4. Sauvegarder la commande en BDD (pour récupérer son ID)
        CommandeFournisseur savedCommande = commandeRepo.save(commande);

        // 5. Enregistrer chaque ligne avec les bonnes références
        for (LigneCommandeFournisseurDto ligneDto : lignesDto) {
            LigneCommandeFournisseur ligne = new LigneCommandeFournisseur();

            ligne.setQtct(ligneDto.getQtct());
            ligne.setQuantite(ligneDto.getQuantite());
            ligne.setPrixAchat(ligneDto.getPrixAchat());
            ligne.setPrixRvt(ligneDto.getPrixRvt());
            ligne.setPourcentageMarge(ligneDto.getPourcentageMarge());
            ligne.setPrixVente(ligneDto.getPrixVente());
            ligne.setMontant(ligneDto.getMontant());

            ligne.setCommandeFournisseur(savedCommande); // 🔥 très important
            ligne.setEntreprise(entreprise); // 🔥 ici tu mets l'entreprise transmise par la commande

            // Récupérer l'article
            Article article = articleRepo.findById(ligneDto.getArticle_id())
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
            ligne.setArticle(article);

            // Enregistrement de la ligne
            ligneRepo.save(ligne);
        }

        return savedCommande;
    }

}
