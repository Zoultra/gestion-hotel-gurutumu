package com.ikadjate.backend.controller;

import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.service.EntrepriseService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ENTREPRISE)
public class EntrepriseController {
    private final EntrepriseService entrepriseService;

    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping
    public List<Entreprise> getAllEntreprises() {
        return entrepriseService.getAllEntreprises();
    }

    @GetMapping("/{id}")
    public Entreprise getEntrepriseById(@PathVariable Long id) {
        return entrepriseService.getEntrepriseById(id);
    }

    @PostMapping
    public Entreprise createEntreprise(@RequestBody Entreprise entreprise) {
        return entrepriseService.saveEntreprise(entreprise);
    }

    @DeleteMapping("/{id}")
    public void deleteEntreprise(@PathVariable Long id) {
        entrepriseService.deleteEntreprise(id);
    }
}
