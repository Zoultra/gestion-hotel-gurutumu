package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.PaiementRequestDto;
import com.ikadjate.backend.model.Facture;
import com.ikadjate.backend.model.Paiement;
import com.ikadjate.backend.model.StatutFacture;
import com.ikadjate.backend.repository.FactureRepository;
import com.ikadjate.backend.repository.PaiementRepository;

import jakarta.transaction.Transactional;

@Service
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final FactureRepository factureRepository;

    public PaiementService(PaiementRepository paiementRepository, FactureRepository factureRepository) {
        this.paiementRepository = paiementRepository;
        this.factureRepository = factureRepository;
    }
    
    
    @Autowired
    private CaisseService caisseService;

    @Transactional
    public Paiement ajouterPaiement(Long factureId, PaiementRequestDto request) {
        Facture facture = factureRepository.findById(factureId)
            .orElseThrow(() -> new RuntimeException("Facture non trouvée"));

        if (request.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Montant du paiement doit être positif");
        }

        // Vérifier que le paiement ne dépasse pas le reste à payer
        if (request.getMontant().compareTo(facture.getResteAPayer()) > 0) {
            throw new IllegalArgumentException("Montant dépasse le reste à payer");
        }

        Paiement paiement = new Paiement();
        paiement.setDatePaiement(request.getDatePaiement());
        paiement.setMontant(request.getMontant());
        paiement.setFacture(facture);
        paiement.setCommentaire(request.getCommentaire());
        paiement.setModePaiement(request.getModePaiement());
       
        paiement = paiementRepository.save(paiement);
        
       

        // Mettre à jour la facture
        BigDecimal montantPaye = facture.getMontantPaye().add(request.getMontant());
        facture.setMontantPaye(montantPaye);

        BigDecimal reste = facture.getMontantNet().subtract(montantPaye);
        facture.setResteAPayer(reste.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : reste);

        // Mettre à jour le statut de la facture
        if (reste.compareTo(BigDecimal.ZERO) == 0) {
            facture.setStatut(StatutFacture.PAYEE);
        } else {
            facture.setStatut(StatutFacture.EN_ATTENTE);
        }

        factureRepository.save(facture);
        
         
        caisseService.enregistrerMouvementPaiement(paiement);
         
        

        return paiement;
    }
    
    
}
