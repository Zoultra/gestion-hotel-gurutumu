package com.ikadjate.backend.repository;

import java.time.LocalDate;
import java.util.List;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ikadjate.backend.model.Vente;

public interface VenteRepository extends JpaRepository<Vente, Long> {
	
	 List<Vente> findByDateVente(LocalDate dateVente);
	 

	 
	 List<Vente> findTop50ByOrderByDateVenteDesc();

}
