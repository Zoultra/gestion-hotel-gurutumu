package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.model.Caisse;
import com.ikadjate.backend.model.OperationType;
import com.ikadjate.backend.model.TypeMouvement;
import com.ikadjate.backend.repository.CaisseRepository;
import com.ikadjate.backend.repository.ChambreRepository;
import com.ikadjate.backend.repository.MouvementCaisseRepository;
import com.ikadjate.backend.repository.SalleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
	
	private final ChambreRepository chambreRepository;
	private final SalleRepository salleRepository;
	private final MouvementCaisseRepository mouvementCaisseRepository;
	private final CaisseRepository caisseRepository;
	 
	public long getNombreTotalChambresDispo() {
	    return chambreRepository.countChambresDisponible();
	}
	
	public long getNombreTotalChambresTotal() {
	    return chambreRepository.countChambresTotal();
	}
	
	public long getNombreTotalSalles() {
	    return salleRepository.countSalles();
	}
	
	public long getNombreTotalSallesDispo() {
	    return salleRepository.countSallesDisponible();
	}
	
	
//	public BigDecimal calculerTotalEntreeParIntervalle1(LocalDate startDate, LocalDate endDate) {
	   
	//    return mouvementCaisseRepository.findTotalMontantByTypeAndDateBetween(TypeMouvement.ENTREE, startDate, endDate);
	//}
	
	public BigDecimal calculerTotalEntreeParIntervalle(LocalDateTime startDateTime, LocalDateTime endDateTime) {
	    return mouvementCaisseRepository.findTotalMontantByTypeAndDateTimeBetween(
	        TypeMouvement.ENTREE, startDateTime, endDateTime
	    );
	}
	
	
	//public BigDecimal calculerTotalDepenseParIntervalle(LocalDateTime startDate, LocalDate endDate) {
		   
	  //  return mouvementCaisseRepository.findTotalDepenseByTypeAndDateBetween(TypeMouvement.SORTIE, startDate, endDate);
	//}
	

	public BigDecimal calculerTotalDepenseParIntervalle(LocalDateTime startDateTime, LocalDateTime endDateTime) {
		   
	    return mouvementCaisseRepository.findTotalDepenseByTypeAndDateTimeBetween(TypeMouvement.SORTIE, startDateTime, endDateTime);
	}
	
	 public BigDecimal getSolde() {
	    	
	       // BigDecimal entrees = mouvementRepository. totalMontantParType(TypeMouvement.ENTREE);
	      //  BigDecimal sorties = mouvementRepository. totalMontantParType(TypeMouvement.SORTIE);
	        
	        List<Caisse> caisses = caisseRepository.findAll();
	        
	        Optional<Caisse> derniereCaisseDeLappli = caisses.stream()
	                .filter(c -> c.getDateFermeture() != null)
	                .max(Comparator.comparing(Caisse::getDateFermeture).thenComparing(Caisse::getHeureFermeture));
	        

	            BigDecimal soldeFinalEnCaisse = derniereCaisseDeLappli.map(Caisse::getSoldeFinal).orElse(BigDecimal.ZERO);
	          
	            return  soldeFinalEnCaisse;
	    }
	 
	 public BigDecimal calculerTotalMontantVenteByInterval (LocalDateTime startDateTime, LocalDateTime endDateTime) {
		   
		    return mouvementCaisseRepository.findTotalMontantByTypeAndDateBetweenVente(TypeMouvement.ENTREE, startDateTime, endDateTime);
		}
	 
	 public BigDecimal calculerTotalMontantReservationByInterval (LocalDateTime startDateTime, LocalDateTime endDateTime) {
		   
		    return mouvementCaisseRepository.findTotalMontantByTypeAndDateBetweenReservation(TypeMouvement.ENTREE, startDateTime, endDateTime);
		}

}
