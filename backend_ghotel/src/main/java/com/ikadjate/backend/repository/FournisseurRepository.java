package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Fournisseur;
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
	
	boolean existsByEmailAndEntrepriseId(String email, Long entrepriseId);
	boolean existsByTelephoneAndEntrepriseId(String telephone, Long entrepriseId);
     
}
