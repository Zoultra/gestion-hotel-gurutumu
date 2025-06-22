package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.DepenseDto;
import com.ikadjate.backend.model.Depense;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.model.TypeMouvement;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.repository.DepenseRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepenseService {

    private final DepenseRepository depenseRepository;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private CaisseService caisseService;
    
    public List<Depense> getAllDepenses() {
        return depenseRepository.findAll();
    }
    
    public List<Depense> getAllDepensesDesc() {
        return depenseRepository.findAllByOrderByCreatedAtDesc();
    }

    public Depense getDepenseById(Long id) {
        return depenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dépense introuvable"));
    }

    @Transactional
    public Depense createDepense(DepenseDto request) {
        // 1. Créer la dépense
        Depense depense = new Depense();
        depense.setLibelle(request.getLibelle());
        depense.setCategorie(request.getCategorie());
        depense.setMontant(request.getMontant());
        depense.setDateDepense(request.getDateDepense());
        depense.setDescription(request.getDescription());
        
        // 2. Sauvegarder la dépense
        Depense savedDepense = depenseRepository.save(depense);
        
        // 3. Enregistrer le mouvement de caisse avec l'utilisateur connecté
        caisseService.enregistrerMouvementDepense(savedDepense);
        
        return savedDepense;
    }

    public Depense updateDepense(Long id, DepenseDto depenseDetails) {
        Depense depense = getDepenseById(id);
        depense.setLibelle(depenseDetails.getLibelle());
        depense.setCategorie(depenseDetails.getCategorie());
        depense.setMontant(depenseDetails.getMontant());
        depense.setDateDepense(depenseDetails.getDateDepense());
        depense.setDescription(depenseDetails.getDescription());

        return depenseRepository.save(depense);
    }

    public void deleteDepense(Long id) {
        depenseRepository.deleteById(id);
    }
}
