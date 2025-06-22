package com.ikadjate.backend.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ikadjate.backend.model.Caisse;

public interface CaisseRepository extends JpaRepository<Caisse, Long> {
	
    Optional<Caisse> findByOuverteTrue();
    
    @Query("SELECT c FROM Caisse c WHERE c.ouverte = true AND c.id = :id")
    Optional<Caisse> findByIdAndOuverteTrue(@Param("id") Long id);
    
    @Query("SELECT c FROM Caisse c WHERE c.dateOuverture = :date OR c.dateFermeture = :date")
	 List<Caisse> findByDateOuvertureOrFermeture(@Param("date") LocalDate date);
    
    
    @Query("SELECT c FROM Caisse c WHERE DATE(c.dateOuverture) = :date")
    List<Caisse> findByDateOuverture(@Param("date") LocalDate date);
    
    @Query("""
    	    SELECT c FROM Caisse c
    	    WHERE (
    	        (c.dateOuverture <= :endDate AND c.dateFermeture >= :startDate)
    	    )
    	""")
    	List<Caisse> findCaissesActivesDansIntervalle(
    	    @Param("startDate") LocalDate startDate,
    	    @Param("endDate") LocalDate endDate
    	);
    
    @Query("SELECT c FROM Caisse c WHERE c.dateOuverture = :today OR c.dateFermeture = :today")
    List<Caisse> findAllOuvertesOuFermeesAujourdHui(@Param("today") LocalDate today);

    
    
}
