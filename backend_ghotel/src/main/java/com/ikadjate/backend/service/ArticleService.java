package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ikadjate.backend.dto.ArticleDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.Categorie;
import com.ikadjate.backend.model.Entreprise;
import com.ikadjate.backend.repository.ArticleRepository;
import com.ikadjate.backend.repository.CategorieRepository;
import com.ikadjate.backend.repository.EntrepriseRepository;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;
    
    @Value("${app.upload.dir}")
    private String uploadDirectory;

    
    public Article saveArticle(String designation, BigDecimal prixUnitaire, MultipartFile image) throws IOException {
        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path imagePath = Paths.get(uploadDirectory, fileName);
        try {
			Files.copy(image.getInputStream(), imagePath);
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Article article = new Article();
        article.setDesignation(designation);
        article.setPrixUnitaire(prixUnitaire);
        article.setImagePath(fileName);

        return articleRepository.save(article);
    }
    
    public Article updateArticleImage(Long id, String designation, BigDecimal prixUnitaire, MultipartFile image) throws IOException {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            throw new RuntimeException("Article non trouvé avec ID : " + id);
        }

        Article article = optionalArticle.get();
        article.setDesignation(designation);
        article.setPrixUnitaire(prixUnitaire);

        if (image != null && !image.isEmpty()) {
            // Supprimer l'ancienne image si nécessaire
            if (article.getImagePath() != null) {
                Path oldImagePath = Paths.get(uploadDirectory, article.getImagePath());
                try {
					Files.deleteIfExists(oldImagePath);
				} catch (java.io.IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

            // Sauvegarde de la nouvelle image
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path newImagePath = Paths.get(uploadDirectory, fileName);
            try {
				Files.copy(image.getInputStream(), newImagePath);
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            article.setImagePath(fileName);
        }

        return articleRepository.save(article);
    }
    

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article introuvable"));
    }

   
    public Article createArticle(ArticleDto request) {
    	
    	String designationNormalise = designationNormalise(request.getDesignation());
    	
    	if (articleRepository.existsByDesignationIgnoreCase(designationNormalise(request.getDesignation()))) {
	        throw new RuntimeException("Un article avec cette designation existe déjà.");
	    }
    	
        Article article = new Article();
        // Mapper les champs de l'ArticleDto vers l'Article
        article.setDesignation(designationNormalise);
       // article.setDesignation(request.getDesignation());
        article.setPrixUnitaire(request.getPrixUnitaire());
        
        // Sauvegarder l'article sans photo
        return articleRepository.save(article);
    }
    
    private String designationNormalise(String designation) {
        return designation == null ? null : designation.trim().replaceAll("\\s+", " ").toUpperCase();
    }

    public Article updateArticle(Long id, Article updatedArticle) {
        Article existing = getArticleById(id);

        // Copie des champs pertinents
        existing.setDesignation(updatedArticle.getDesignation());
        existing.setPrixUnitaire(updatedArticle.getPrixUnitaire());
        
        
        return articleRepository.save(existing);
   }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
