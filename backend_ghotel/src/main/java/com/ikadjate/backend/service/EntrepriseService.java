package com.ikadjate.backend.service;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.repository.EntrepriseRepository;

import java.util.List;

@Service
public class EntrepriseService {
    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Entreprise getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id).orElse(null);
    }

    public Entreprise saveEntreprise(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }

    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }
}
