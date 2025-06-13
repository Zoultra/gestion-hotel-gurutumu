package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Chambre;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long>{
	
	@Query("SELECT COUNT(c) FROM Chambre c WHERE c.etat = 'DISPONIBLE'")
	long countChambresDisponible();
	
	@Query("SELECT COUNT(c) FROM Chambre c ")
	long countChambresTotal();
	
	boolean existsByNumeroIgnoreCase(String numero);

}
