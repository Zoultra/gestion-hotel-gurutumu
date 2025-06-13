package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.SalleDto;
import com.ikadjate.backend.model.Salle;
import com.ikadjate.backend.repository.SalleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalleService {

    private final SalleRepository salleRepository;

    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public Salle getSalleById(Long id) {
        return salleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Salle introuvable"));
    }

    public Salle createSalle(SalleDto request) {
    	
    	String numeroNormalise = normaliserNumero(request.getNumero());
    	 
    	 // Vérifie si une salle avec ce nom existe déjà
    	if (salleRepository.existsByNumeroIgnoreCase(normaliserNumero(request.getNumero()))) {
    	        throw new RuntimeException("Une salle avec ce numero existe déjà.");
    	    }
    	 
        Salle salle = new Salle();
      //  salle.setNumero(request.getNumero());
        salle.setNumero(numeroNormalise);
      //  salle.setNom(request.getNom());
        salle.setCapaciteMax(request.getCapaciteMax());
        salle.setEquipements(request.getEquipements());
        salle.setType(request.getType());
        salle.setStatut(request.getStatut());
        salle.setTarif(request.getTarif());

        return salleRepository.save(salle);
    }
    
    private String normaliserNumero(String numero) {
        return numero == null ? null : numero.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    public Salle updateSalle(Long id, Salle salleDetails) {
        Salle salle = getSalleById(id);
        salle.setNumero(salleDetails.getNumero());
      //  salle.setNom(salleDetails.getNom());
        salle.setCapaciteMax(salleDetails.getCapaciteMax());
        salle.setEquipements(salleDetails.getEquipements());
        salle.setType(salleDetails.getType());
        salle.setStatut(salleDetails.getStatut());
        salle.setTarif(salleDetails.getTarif());
        return salleRepository.save(salle);
    }

    public void deleteSalle(Long id) {
        salleRepository.deleteById(id);
    }
}
