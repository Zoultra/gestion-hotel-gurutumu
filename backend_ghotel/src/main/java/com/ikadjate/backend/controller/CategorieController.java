package com.ikadjate.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.CategorieDto;
import com.ikadjate.backend.model.Categorie;
import com.ikadjate.backend.service.CategorieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.CATEGORIE)
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService categorieService;

    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Categorie getCategorieById(@PathVariable Long id) {
        return categorieService.getCategorieById(id);
    }

    @PostMapping
    public ResponseEntity<?> createCategorie(@RequestBody CategorieDto request) {
        try {
            Categorie createdCategorie = categorieService.createCategorie(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategorie);
        } catch (RuntimeException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public Categorie updateCategorie(@PathVariable Long id, @RequestBody Categorie categorieDetails) {
        return categorieService.updateCategorie(id, categorieDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
    }
}
