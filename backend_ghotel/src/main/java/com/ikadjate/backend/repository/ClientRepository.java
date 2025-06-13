package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ikadjate.backend.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	 
     
}
