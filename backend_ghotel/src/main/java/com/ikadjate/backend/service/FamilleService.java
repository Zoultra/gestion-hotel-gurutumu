package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.FamilleDto;
import com.ikadjate.backend.model.Famille;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.repository.FamilleRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FamilleService {
    
    private final FamilleRepository familleRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    public List<Famille> getAllFamilles() {
        return familleRepository.findAll();
    }

    public Famille getFamilleById(Long id) {
        return familleRepository.findById(id).orElseThrow(() -> new RuntimeException("Famille introuvable"));
    }

    public Famille createFamille(FamilleDto request) {
        Entreprise entreprise = entrepriseRepository.findById(request.getEntreprise_id())
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        // Exemple : empêcher la duplication
        if (familleRepository.existsByDesignationAndEntrepriseId(request.getDesignation(), entreprise.getId())) {
            throw new RuntimeException("Une famille avec ce nom existe déjà pour cette entreprise.");
        }

        Famille famille = new Famille();
        famille.setDesignation(request.getDesignation());
        famille.setDescription(request.getDescription());
        famille.setEntreprise(entreprise);

        return familleRepository.save(famille);
    }

    public Famille updateFamille(Long id, Famille familleDetails) {
        Famille famille = getFamilleById(id);
        famille.setDesignation(familleDetails.getDesignation());
        famille.setDescription(familleDetails.getDescription());
        return familleRepository.save(famille);
    }

    public void deleteFamille(Long id) {
        familleRepository.deleteById(id);
    }
}
