package com.ikadjate.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ikadjate.backend.model.LigneVente;
 

public interface LigneVenteRepository extends JpaRepository<LigneVente, Long>{
	
	 List<LigneVente> findByVenteId(Long id);
	 
	 @Modifying
	 @Query("DELETE FROM LigneVente l WHERE l.vente.id = :venteId")
	 void deleteByVenteId(@Param("venteId") Long venteId);
}
