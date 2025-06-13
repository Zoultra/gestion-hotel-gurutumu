package com.ikadjate.backend.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikadjate.backend.dto.CommandeClientDto;
import com.ikadjate.backend.dto.CommandeFournisseurDto;
import com.ikadjate.backend.dto.LigneCommandeClientDto;
import com.ikadjate.backend.dto.LigneCommandeFournisseurDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.Client;
import com.ikadjate.backend.model.CommandeClient;
import com.ikadjate.backend.model.CommandeFournisseur;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.model.Fournisseur;
import com.ikadjate.backend.model.LigneCommandeClient;
import com.ikadjate.backend.model.LigneCommandeFournisseur;
import com.ikadjate.backend.repository.ArticleRepository;
import com.ikadjate.backend.repository.ClientRepository;
import com.ikadjate.backend.repository.CommandeClientRepository;
import com.ikadjate.backend.repository.CommandeFournisseurRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;
import com.ikadjate.backend.repository.FournisseurRepository;
import com.ikadjate.backend.repository.LigneCommandeClientRepository;
import com.ikadjate.backend.repository.LigneCommandeFournisseurRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CommandeClientService {

    private final CommandeClientRepository commandeRepository;
    private final LigneCommandeClientRepository ligneCommandeRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;

    public CommandeClientService(CommandeClientRepository commandeRepository,
    		LigneCommandeClientRepository ligneCommandeRepository,
                                 ClientRepository clientRepository,
                                 ArticleRepository articleRepository) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public CommandeClientDto save(CommandeClientDto dto) {
        // 1. Chercher le client
        var client = clientRepository.findById(dto.getClient().getId())
            .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // 2. Enregistrer la commande
        CommandeClient commande = new CommandeClient();
        commande.setEtatCommande(dto.getEtatCommande());
        commande.setClient(client);
        commande.setMontantTotal(dto.getMontantTotal());
        commande.setDateCommande(dto.getDateCommande());
        commande = commandeRepository.save(commande);

        // 3. Enregistrer les lignes
        for (LigneCommandeClientDto ligneDto : dto.getLignes()) {
            var article = articleRepository.findById(ligneDto.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));

            LigneCommandeClient ligne = new LigneCommandeClient();
            ligne.setCommandeClient(commande);
            ligne.setArticle(article);
            ligne.setQuantite(ligneDto.getQuantite());
            ligne.setPrixUnitaire(ligneDto.getPrixUnitaire());
            ligneCommandeRepository.save(ligne);
        }

        return dto;
    }
    
    public List<CommandeClient> getCommandesClient() {
        return commandeRepository.findAll();
    }
    
    public CommandeClient getCommandesClientById(Long id) {
        return commandeRepository.findById(id).orElseThrow(() -> new RuntimeException("Commande Client introuvable"));
    }
}