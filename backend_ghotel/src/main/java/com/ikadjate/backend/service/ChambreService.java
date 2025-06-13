package com.ikadjate.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.ChambreDto;
import com.ikadjate.backend.model.Chambre;
import com.ikadjate.backend.repository.ChambreRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChambreService {

    private final ChambreRepository chambreRepository;

    
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    public Chambre getChambreById(Long id) {
        return chambreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Chambre introuvable"));
    }

    public Chambre createChambre(ChambreDto request) {
    	
    	String numeroNormalise = normaliserNumero(request.getNumero());
    	
    	if (chambreRepository.existsByNumeroIgnoreCase(normaliserNumero(request.getNumero()))) {
	        throw new RuntimeException("Une chambre avec ce numero existe déjà.");
	    }
    	
    	 
        Chambre chambre = new Chambre();
       // chambre.setNumero(request.getNumero());
        chambre.setNumero(numeroNormalise);
        chambre.setType(request.getType());
        chambre.setTarifParNuit(request.getTarifParNuit());
        chambre.setEtat(request.getEtat());
        chambre.setDescription(request.getDescription());

        return chambreRepository.save(chambre);
    }
    
    private String normaliserNumero(String numero) {
        return numero == null ? null : numero.trim().replaceAll("\\s+", " ").toUpperCase();
    }
    
    
    public Chambre updateChambre(Long id, Chambre chambreDetails) {
        Chambre chambre = getChambreById(id);
        chambre.setNumero(chambreDetails.getNumero());
        chambre.setType(chambreDetails.getType());
        chambre.setTarifParNuit(chambreDetails.getTarifParNuit());
        chambre.setEtat(chambreDetails.getEtat());
        chambre.setDescription(chambreDetails.getDescription());

        return chambreRepository.save(chambre);
    }

    public void deleteChambre(Long id) {
        chambreRepository.deleteById(id);
    }
}
