package com.ikadjate.backend.repository;

 

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
 
import org.springframework.stereotype.Repository;
import com.ikadjate.backend.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long>  {
	
	 
	 List<Reservation> findAllByOrderByIdDesc();

}


