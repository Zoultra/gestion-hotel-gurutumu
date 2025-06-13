package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ikadjate.backend.model.Depense;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long>{

}
