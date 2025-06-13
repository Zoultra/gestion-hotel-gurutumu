package com.ikadjate.backend.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.ArticleDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.service.ArticleService;

import io.jsonwebtoken.io.IOException;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.UrlResource;




import org.springframework.beans.factory.annotation.Value;
 
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

 
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;




@RestController
@RequestMapping(ApiPaths.ARTICLE)
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    
    @Value("${app.upload.dir}")
    private String uploadDirectory;

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Article> uploadArticle(
            @RequestParam("designation") String designation,
            @RequestParam("prixUnitaire") BigDecimal prixUnitaire,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            Article article = articleService.saveArticle(designation, prixUnitaire, image);
            return ResponseEntity.ok(article);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<UrlResource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(filename).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = null;
			try {
				contentType = Files.probeContentType(filePath);
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(articleService.getArticleById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

     
    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody ArticleDto request) {
        try {
            Article createdArticle = articleService.createArticle(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
  
    
    // Liste tous les articles (sans images)
    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        List<ArticleDto> dtos = articles.stream().map(article -> {
            ArticleDto dto = new ArticleDto();
            dto.setId(article.getId());
            dto.setDesignation(article.getDesignation());
            dto.setPrixUnitaire(article.getPrixUnitaire());
            dto.setImagePath(article.getImagePath());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/upload/{id}")
    public ResponseEntity<Article> updateArticleImage(
            @PathVariable Long id,
            @RequestParam("designation") String designation,
            @RequestParam("prixUnitaire") BigDecimal prixUnitaire,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Article updatedArticle = articleService.updateArticleImage(id, designation, prixUnitaire, image);
        return ResponseEntity.ok(updatedArticle);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        try {
            Article updatedArticle = articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
