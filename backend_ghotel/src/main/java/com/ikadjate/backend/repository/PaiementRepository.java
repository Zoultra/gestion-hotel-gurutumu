package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Paiement;

@Repository
public interface PaiementRepository extends JpaRepository <Paiement, Long> {

}
