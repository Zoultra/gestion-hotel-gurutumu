package com.ikadjate.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ikadjate.backend.dto.VenteDto;
import com.ikadjate.backend.auth.AuthService;
import com.ikadjate.backend.dto.LigneVenteDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.Client;
import com.ikadjate.backend.model.EtatVente;
import com.ikadjate.backend.model.Vente;
import com.ikadjate.backend.model.LigneVente;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.model.TypeMouvement;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.repository.ArticleRepository;
import com.ikadjate.backend.repository.ClientRepository;
import com.ikadjate.backend.repository.VenteRepository;

import jakarta.persistence.EntityNotFoundException;

import com.ikadjate.backend.repository.LigneVenteRepository;
import com.ikadjate.backend.repository.MouvementCaisseRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenteService {

    private final VenteRepository venteRepository;
    private final LigneVenteRepository ligneVenteRepository;
    
    private final ArticleRepository articleRepository;
    @Autowired
    private CaisseService caisseService;
    
    private final UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private MouvementCaisseRepository mouvementRepository;
    
    @Autowired
    private AuthService authService;
    
    public VenteService(VenteRepository venteRepository,
                        LigneVenteRepository ligneVenteRepository,
                        ArticleRepository articleRepository,
                        UtilisateurRepository utilisateurRepository) {
        this.venteRepository = venteRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.articleRepository = articleRepository;
        this.utilisateurRepository = utilisateurRepository;
    }
    
    
    public VenteDto payerVente(Long id) {
        Vente vente = venteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vente non trouvée"));

        if (!vente.getEtatVente().equals(EtatVente.PAYEE)) {
            vente.setEtatVente(EtatVente.PAYEE);
            vente.setMontantPaye(vente.getMontantTotal());
            vente.setMonnaie(
                vente.getMonnaie().compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : vente.getMonnaie()
            );

            // Enregistrer mouvement de caisse ici
            caisseService.enregistrerMouvementVente(vente);
            vente = venteRepository.save(vente);
        }

        return mapToDto(vente);
    }

    

    @Transactional
    public VenteDto save(VenteDto dto) {
        
    	 // 1. Vérification de l'utilisateur courant
        Utilisateur utilisateur;
        try {
            utilisateur = authService.getCurrentUser();
            if (utilisateur == null) {
                throw new UsernameNotFoundException("Aucun utilisateur authentifié");
            }
        } catch (UsernameNotFoundException e) {
                 throw new SecurityException("Session expirée ou invalide. Veuillez vous reconnecter.");
        }

        // 2. Enregistrer la vente
        Vente vente = new Vente();
         
        vente.setEtatVente(dto.getEtatVente());
        vente.setCode(dto.getCode());
        vente.setMontantTotal(dto.getMontantTotal());
        vente.setMontantPaye(dto.getMontantPaye());
        if (dto.getMonnaie().compareTo(BigDecimal.ZERO) < 0) {
            vente.setMonnaie(BigDecimal.ZERO); // Si monnaie < 0, on force à 0
        } else {
            vente.setMonnaie(dto.getMonnaie()); // Sinon on enregistre la valeur normale
        }
        vente.setTableResto(dto.getTableResto());
        vente.setDateVente(dto.getDateVente());
        
        vente = venteRepository.save(vente);
        
     // Creation du mouvement caisse
        if (dto.getMontantPaye().compareTo(BigDecimal.ZERO) != 0) {
            // Montant payé est différent de zéro
        	 caisseService.enregistrerMouvementVente(vente);
        }
       

        // 3. Enregistrer les lignes de vente
        for (LigneVenteDto ligneDto : dto.getLignes()) {
            var article = articleRepository.findById(ligneDto.getArticle_id())
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));

            LigneVente ligne = new LigneVente();
            ligne.setVente(vente);
            ligne.setArticle(article);
            ligne.setQuantite(ligneDto.getQuantite());
            ligne.setPrixUnitaire(ligneDto.getPrixUnitaire());
            ligneVenteRepository.save(ligne);
        }

        return dto;
    }

    public List<Vente> getVentes() {
        return venteRepository.findAllByOrderByCreatedAtDesc();
    }

    public Vente getVenteById(Long id) {
        return venteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vente introuvable"));
    }
    
 
   public void supprimerVente(Long id) {
	    Vente vente = venteRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Vente introuvable"));

	    venteRepository.delete(vente); // Cela supprimera aussi les lignes liées
	}
   
   public LigneVenteDto toDto(LigneVente entity) {
	    LigneVenteDto dto = new LigneVenteDto();
	    dto.setId(entity.getId());
	    dto.setPrixUnitaire(entity.getPrixUnitaire());
	    dto.setQuantite(entity.getQuantite());
	    dto.setVente_id(entity.getVente().getId()); // pas l'objet Vente complet
	    return dto;
	}
   
   public List<LigneVenteDto> findAllByVenteId(Long venteId) {
	    return ligneVenteRepository.findByVenteId(venteId).stream()
	            .map(this::toDto)
	            .collect(Collectors.toList());
	}
   
   
   public List<Vente> getLast50Ventes() {
	    return venteRepository.findTop10ByOrderByCreatedAtDesc();
	}
    
   // MISE A JOUR DE LA VENTE
   
   @Transactional
   public VenteDto updateVente(Long id, VenteDto dto) {

       // 1. Récupérer la vente existante
       Vente vente = venteRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée avec l'ID: " + id));

       // 2. Supprimer les anciennes lignes de vente
       ligneVenteRepository.deleteByVenteId(vente.getId());
       venteRepository.flush(); // Force la suppression immédiate

       // 3. Recréer les lignes de vente
       List<LigneVente> nouvellesLignes = new ArrayList<>();
       for (LigneVenteDto ligneDto : dto.getLignes()) {
           Article article = articleRepository.findById(ligneDto.getArticle_id())
                   .orElseThrow(() -> new EntityNotFoundException("Article non trouvé avec l'ID: " + ligneDto.getArticle_id()));
           
           LigneVente ligne = new LigneVente();
           ligne.setVente(vente);
           ligne.setArticle(article);
           ligne.setQuantite(ligneDto.getQuantite());
           ligne.setPrixUnitaire(ligneDto.getPrixUnitaire());
           nouvellesLignes.add(ligne);
       }
       ligneVenteRepository.saveAll(nouvellesLignes);

       // 4. Mettre à jour les informations de base
       vente.setEtatVente(dto.getEtatVente());
       vente.setMontantTotal(dto.getMontantTotal());
       vente.setDateVente(dto.getDateVente());
       vente.setTableResto(dto.getTableResto());

       // 5. Si la vente est PAYÉE, gérer les mouvements de caisse
       if (dto.getEtatVente() == EtatVente.PAYEE) {
           // Supprimer l'ancien mouvement s’il existe
           MouvementCaisse ancienMouvement = mouvementRepository.findByVenteId(vente.getId())
                   .stream().findFirst().orElse(null);

           if (ancienMouvement != null) {
               mouvementRepository.deleteById(ancienMouvement.getId());
               mouvementRepository.flush();
           }

           // Enregistrer le nouveau mouvement
           caisseService.enregistrerMouvementVente(vente);
       }

       // 6. Sauvegarder la vente mise à jour
       vente = venteRepository.save(vente);

       // 7. Retourner le DTO
       return mapToDto(vente);
   }


   private VenteDto mapToDto(Vente venteupdated) {
       VenteDto dto = new VenteDto();
       dto.setId(venteupdated.getId());
       dto.setCode(venteupdated.getCode());
       dto.setDateVente(venteupdated.getDateVente());
       dto.setMontantTotal(venteupdated.getMontantTotal());
       dto.setEtatVente(venteupdated.getEtatVente());
       dto.setTableResto(venteupdated.getTableResto());
       
       // Mapper les lignes si nécessaire
       return dto;
   }
    
}
