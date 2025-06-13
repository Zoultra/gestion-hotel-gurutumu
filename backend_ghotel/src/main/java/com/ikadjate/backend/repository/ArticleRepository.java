package com.ikadjate.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.LigneVente;
 


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
	
	boolean existsByDesignationIgnoreCase(String designation);

}
