package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

 
import com.ikadjate.backend.model.Salle;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long>{
          
	@Query("SELECT COUNT(s) FROM Salle s")
	long countSalles();
	
	@Query("SELECT COUNT(s) FROM Salle s WHERE s.statut = 'DISPONIBLE'")
	long countSallesDisponible();
	
	boolean existsByNumeroIgnoreCase(String numero);
}
