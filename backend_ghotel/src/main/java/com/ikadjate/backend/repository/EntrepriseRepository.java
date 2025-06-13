package com.ikadjate.backend.repository;

 
import org.springframework.data.jpa.repository.JpaRepository;

import com.ikadjate.backend.model.Entreprise;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
}
