package com.ikadjate.backend.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Caisse;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.model.OperationType;
import com.ikadjate.backend.model.TypeMouvement;

@Repository
public interface MouvementCaisseRepository extends JpaRepository <MouvementCaisse, Long>{
    
    @Query("SELECT SUM(m.montant) FROM MouvementCaisse m WHERE m.type = :type")
    BigDecimal totalMontantParType(@Param("type") TypeMouvement type);
    
    
    
    @Query("SELECT " +
    	       "COALESCE(SUM(CASE WHEN m.type = com.ikadjate.backend.model.TypeMouvement.ENTREE THEN m.montant ELSE 0 END), 0) - " +
    	       "COALESCE(SUM(CASE WHEN m.type = com.ikadjate.backend.model.TypeMouvement.SORTIE THEN m.montant ELSE 0 END), 0) " +
    	       "FROM MouvementCaisse m WHERE m.date <= :date")
    	BigDecimal calculerSoldeJusquaDate(@Param("date") LocalDate date);

    	List<MouvementCaisse> findByDate(LocalDate date);
    	
    	@Query("SELECT SUM(m.montant) FROM MouvementCaisse m WHERE m.caisse.id = :caisseId AND m.type = :type")
    	BigDecimal totalByCaisseAndType(@Param("caisseId") Long caisseId, @Param("type") TypeMouvement type);
    	
    	 List<MouvementCaisse> findByCaisse(Caisse caisse);
    	 
    	 @Query("SELECT m FROM MouvementCaisse m WHERE m.caisse = :caisse AND DATE(m.date) = :date")
    	 List<MouvementCaisse> findByCaisseAndDate(@Param("caisse") Caisse caisse, @Param("date") LocalDate date);
    	 
    	 
    	// Pour les réservations
    	 @Query("SELECT m FROM MouvementCaisse m WHERE m.reservationId = :reservationId")
    	 List<MouvementCaisse> findByReservationId(@Param("reservationId") Long reservationId);

    	 // Pour les ventes
    	 @Query("SELECT m FROM MouvementCaisse m WHERE m.venteId = :venteId")
    	 List<MouvementCaisse> findByVenteId(@Param("venteId") Long venteId);

    	 // Pour les dépenses
    	 @Query("SELECT m FROM MouvementCaisse m WHERE m.depenseId = :depenseId")
    	 List<MouvementCaisse> findByDepenseId(@Param("depenseId") Long depenseId);
    	 
    	 @Modifying
    	 @Query("DELETE FROM MouvementCaisse m WHERE m.reservationId = :reservationId")
    	 void deleteByReservationId(@Param("reservationId") Long reservationId);
    	 
    	 @Modifying
    	 @Query("DELETE FROM MouvementCaisse m WHERE m.venteId = :venteId")
    	 void deleteByVenteId(@Param("venteId") Long venteId);
    	 
    	 // DASHBOARD REQUETTES
    	 
    	 @Query("SELECT COALESCE(SUM(m.montant), 0) FROM MouvementCaisse m " +
    		       "WHERE m.type = :type AND m.date >= :start AND m.date <= :end")
    		BigDecimal findTotalMontantByTypeAndDateBetween(
    		    @Param("type") TypeMouvement type,
    		    @Param("start") LocalDate  start,
    		    @Param("end") LocalDate  end
    		);
    	 
    	 @Query("SELECT COALESCE(SUM(m.montant), 0) FROM MouvementCaisse m " +
  		       "WHERE m.type = :type AND m.date >= :start AND m.date <= :end")
  		   BigDecimal findTotalDepenseByTypeAndDateBetween(
  		    @Param("type") TypeMouvement type,
  		    @Param("start") LocalDate  start,
  		    @Param("end") LocalDate  end
  		   );
    	 
    	 @Query("SELECT COALESCE(SUM(m.montant), 0) FROM MouvementCaisse m " +
  		       "WHERE m.type = :type AND m.operationType = 'VENTE' AND m.date >= :start AND m.date <= :end")
  		 BigDecimal findTotalMontantByTypeAndDateBetweenVente(
  		  //  @Param("operationType") OperationType operationType,
  		    @Param("type") TypeMouvement type,
  		    @Param("start") LocalDate  start,
  		    @Param("end") LocalDate  end
  				 );
    	 
    	 @Query("SELECT COALESCE(SUM(m.montant), 0) FROM MouvementCaisse m " +
    		       "WHERE m.type = :type AND m.operationType = 'RESERVATION' AND m.date >= :start AND m.date <= :end")
    		 BigDecimal findTotalMontantByTypeAndDateBetweenReservation(
    		  //  @Param("operationType") OperationType operationType,
    		    @Param("type") TypeMouvement type,
    		    @Param("start") LocalDate  start,
    		    @Param("end") LocalDate  end
    				 );
    	 
    	 @Query("SELECT m FROM MouvementCaisse m WHERE m.caisse = :caisse AND m.date >= :debut AND m.date <= :fin")
    	 List<MouvementCaisse> findByCaisseAndDateTimeRange(@Param("caisse") Caisse caisse, @Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    	 // 
    	 @Query("SELECT COALESCE(SUM(m.montant), 0) FROM MouvementCaisse m " +
    		       "WHERE m.type = :type AND m.createdAt BETWEEN :start AND :end")
    		BigDecimal findTotalMontantByTypeAndDateTimeBetween(@Param("type") TypeMouvement type,
    		                                                    @Param("start") LocalDateTime start,
    		                                                    @Param("end") LocalDateTime end);


}
