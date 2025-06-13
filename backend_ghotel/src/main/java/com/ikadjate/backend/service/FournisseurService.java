package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.FournisseurDto;
import com.ikadjate.backend.model.Fournisseur;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.repository.FournisseurRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FournisseurService {
    private final FournisseurRepository fournisseurRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    public Fournisseur getFournisseurById(Long id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur introuvable"));
    }

    public Fournisseur createFournisseur(FournisseurDto request) {
        // Récupérer l'entreprise
        Entreprise entreprise = entrepriseRepository.findById(request.getEntreprise_id())
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));

        // Vérifier si un fournisseur avec le même email existe déjà pour cette entreprise
        if (fournisseurRepository.existsByEmailAndEntrepriseId(request.getEmail(), entreprise.getId())) {
            throw new RuntimeException("Un fournisseur avec cet email existe déjà pour cette entreprise.");
        }

        // Vérifier si un fournisseur avec le même téléphone existe déjà pour cette entreprise
        if (fournisseurRepository.existsByTelephoneAndEntrepriseId(request.getTelephone(), entreprise.getId())) {
            throw new RuntimeException("Un fournisseur avec ce numéro de téléphone existe déjà pour cette entreprise.");
        }

        // Création du fournisseur
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setNom(request.getNom());
        fournisseur.setEmail(request.getEmail());
        fournisseur.setTelephone(request.getTelephone());
        fournisseur.setObservation(request.getObservation());
        fournisseur.setAdresse(request.getAdresse());
        fournisseur.setEntreprise(entreprise);

        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur updateFournisseur(Long id, Fournisseur fournisseurDetails) {
        Fournisseur fournisseur = getFournisseurById(id);
        fournisseur.setNom(fournisseurDetails.getNom());
        fournisseur.setEmail(fournisseurDetails.getEmail());
        fournisseur.setTelephone(fournisseurDetails.getTelephone());
        fournisseur.setAdresse(fournisseurDetails.getAdresse());
        return fournisseurRepository.save(fournisseur);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
