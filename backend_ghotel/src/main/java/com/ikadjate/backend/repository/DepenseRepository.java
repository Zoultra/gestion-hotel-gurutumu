package com.ikadjate.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Depense;


@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long>{
	
	List<Depense> findAllByOrderByCreatedAtDesc();
	
	 @Query("SELECT d FROM Depense d " +
		       "WHERE d.createdAt BETWEEN :start AND :end")
		List<Depense> findAllByCreatedAtBetween(
		                                                   @Param("start") LocalDateTime start,
		                                                   @Param("end") LocalDateTime end);
	
	 @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d " +
		       "WHERE d.createdAt BETWEEN :start AND :end")
		BigDecimal findTotalDepenseByTypeAndDateTimeBetween( 
		                                                    @Param("start") LocalDateTime start,
		                                                    @Param("end") LocalDateTime end);
}
