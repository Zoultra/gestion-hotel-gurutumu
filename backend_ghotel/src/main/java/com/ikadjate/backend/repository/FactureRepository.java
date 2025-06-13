package com.ikadjate.backend.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Facture;

@Repository
public interface FactureRepository extends JpaRepository <Facture, Long>{
	
	long countByDateFacture(LocalDate dateFacture);
	
	 Optional<Facture> findByReservationId(Long reservationId);

}
