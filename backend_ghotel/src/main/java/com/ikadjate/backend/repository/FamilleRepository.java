package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ikadjate.backend.model.Famille;

 
@Repository
public interface FamilleRepository extends JpaRepository<Famille, Long>{
	
	boolean existsByDesignationAndEntrepriseId(String designation, Long entrepriseId);
	 

}
