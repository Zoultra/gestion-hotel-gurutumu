package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Categorie;

 
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long>{
	
	boolean existsByDesignationAndEntrepriseId(String designation, Long entrepriseId);
	 

}
