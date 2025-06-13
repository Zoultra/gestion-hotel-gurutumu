package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.CategorieDto;
import com.ikadjate.backend.model.Categorie;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.model.Famille;
import com.ikadjate.backend.repository.CategorieRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;
import com.ikadjate.backend.repository.FamilleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategorieService {

    private final CategorieRepository categorieRepository;

    @Autowired
    private FamilleRepository familleRepository;
    
    @Autowired
    private EntrepriseRepository entrepriseRepository;

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public Categorie getCategorieById(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie introuvable"));
    }

    public Categorie createCategorie(CategorieDto request) {
        Entreprise entreprise = entrepriseRepository.findById(request.getEntreprise_id())
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        
        Famille famille = familleRepository.findById(request.getFamille_id())
                .orElseThrow(() -> new RuntimeException("Famille non trouvée"));

        // Vérifier si une catégorie avec le même nom existe déjà pour l'entreprise
        if (categorieRepository.existsByDesignationAndEntrepriseId(request.getDesignation(), entreprise.getId())) {
            throw new RuntimeException("Une catégorie avec cette désignation existe déjà pour votre entreprise.");
        }

        
        
        Categorie categorie = new Categorie();
        categorie.setDesignation(request.getDesignation());
        categorie.setDescription(request.getDescription());
        categorie.setEntreprise(entreprise);
        categorie.setFamille(famille);

        return categorieRepository.save(categorie);
    }

    public Categorie updateCategorie(Long id, Categorie updatedCategorie) {
        Categorie existing = getCategorieById(id);
        existing.setDesignation(updatedCategorie.getDesignation());
        existing.setDescription(updatedCategorie.getDescription());
        return categorieRepository.save(existing);
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
}
