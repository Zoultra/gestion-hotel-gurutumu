package com.ikadjate.backend.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ikadjate.backend.model.LigneReservation;
 

public interface LigneReservationRepository extends JpaRepository <LigneReservation, Long> {
	
	@Query("""
		    SELECT COUNT(lr) FROM LigneReservation lr
		    JOIN lr.reservation r
		    WHERE lr.chambre IS NOT NULL
		      AND lr.chambre.id = :chambreId
		      AND (
		          :dateDebut BETWEEN r.dateDebut AND r.dateFin OR
		          :dateFin BETWEEN r.dateDebut AND r.dateFin OR
		          r.dateDebut BETWEEN :dateDebut AND :dateFin
		      )
		""")
		Long countReservationsChambreEnConflit(@Param("chambreId") Long chambreId,
		                                       @Param("dateDebut") LocalDate dateDebut,
		                                       @Param("dateFin") LocalDate dateFin);

	
	@Query("""
		    SELECT COUNT(lr) FROM LigneReservation lr
		    JOIN lr.reservation r
		    WHERE lr.salle IS NOT NULL
		      AND lr.salle.id = :salleId
		      AND (
		          :dateDebut BETWEEN r.dateDebut AND r.dateFin OR
		          :dateFin BETWEEN r.dateDebut AND r.dateFin OR
		          r.dateDebut BETWEEN :dateDebut AND :dateFin
		      )
		""")
		Long countReservationsSalleEnConflit(@Param("salleId") Long salleId,
		                                     @Param("dateDebut") LocalDate dateDebut,
		                                     @Param("dateFin") LocalDate dateFin);
	
	
	
	 @Query("SELECT COUNT(l) FROM LigneReservation l WHERE " +
	           "l.chambre.id = :idChambre AND " +
	           "l.reservation.id != :reservationId AND " +
	           "((l.reservation.dateDebut BETWEEN :dateDebut AND :dateFin) OR " +
	           "(l.reservation.dateFin BETWEEN :dateDebut AND :dateFin) OR " +
	           "(:dateDebut BETWEEN l.reservation.dateDebut AND l.reservation.dateFin))")
	    Long countReservationsChambreEnConflitExcludingReservation(
	        @Param("idChambre") Long idChambre,
	        @Param("dateDebut") LocalDate dateDebut,
	        @Param("dateFin") LocalDate dateFin,
	        @Param("reservationId") Long reservationId);
	    
	 @Query("SELECT COUNT(l) FROM LigneReservation l WHERE " +
	           "l.salle.id = :idSalle AND " +
	           "l.reservation.id != :reservationId AND " +
	           "((l.reservation.dateDebut BETWEEN :dateDebut AND :dateFin) OR " +
	           "(l.reservation.dateFin BETWEEN :dateDebut AND :dateFin) OR " +
	           "(:dateDebut BETWEEN l.reservation.dateDebut AND l.reservation.dateFin) OR " +
	           "(:dateFin BETWEEN l.reservation.dateDebut AND l.reservation.dateFin))")
	    Long countReservationsSalleEnConflitExcludingReservation(
	        @Param("idSalle") Long idSalle,
	        @Param("dateDebut") LocalDate dateDebut,
	        @Param("dateFin") LocalDate dateFin,
	        @Param("reservationId") Long reservationId);
	 
	 
	  void deleteByReservationId(Long reservationId);


}
