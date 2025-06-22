package com.ikadjate.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ikadjate.backend.config.ApiPaths;
 
import com.ikadjate.backend.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPaths.DASHBOARD)
@RequiredArgsConstructor
public class DashboardController {
	
	private final DashboardService dashboardService;
	 
	
	@GetMapping("/nombre-chambres-dispo")
	public ResponseEntity<Long> getNombreChambresDispo() {
	    return ResponseEntity.ok(dashboardService.getNombreTotalChambresDispo());
	}
	
	@GetMapping("/nombre-chambres-total")
	public ResponseEntity<Long> getNombreChambres() {
	    return ResponseEntity.ok(dashboardService.getNombreTotalChambresTotal());
	}
	
	@GetMapping("/nombre-salles")
	public ResponseEntity<Long> getNombreSalles() {
	    return ResponseEntity.ok(dashboardService.getNombreTotalSalles());
	}
	
	@GetMapping("/nombre-salles-dispo")
	public ResponseEntity<Long> getNombreSallesDispo() {
	    return ResponseEntity.ok(dashboardService.getNombreTotalSallesDispo());
	}
	
//	@GetMapping("/total-entrees")
	//public ResponseEntity<BigDecimal> getTotalEntrees(
	//        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	//        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

	//    BigDecimal total = dashboardService.calculerTotalEntreeParIntervalle(startDate, endDate);
	//    return ResponseEntity.ok(total);
	//}
	@GetMapping("/total-entrees")
	public ResponseEntity<BigDecimal> getTotalEntrees(
	    @RequestParam("startDateTime") 
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,

	    @RequestParam("endDateTime") 
	    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime
	) {
	    BigDecimal total = dashboardService.calculerTotalEntreeParIntervalle(startDateTime, endDateTime);
	    return ResponseEntity.ok(total);
	}


	
	
	@GetMapping("/total-depenses")
	public ResponseEntity<BigDecimal> getTotalDepense(
	        @RequestParam("startDateTime") 
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
	        
	        @RequestParam("endDateTime") 
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {

	    BigDecimal total = dashboardService.calculerTotalDepenseParIntervalle(startDateTime, endDateTime);
	    return ResponseEntity.ok(total);
	}
	
	@GetMapping("/solde")
	    public ResponseEntity<BigDecimal> getSolde() {
	        return ResponseEntity.ok(dashboardService.getSolde());
	    }
	 
	
	@GetMapping("/total-entrees-resto")
	public ResponseEntity<BigDecimal> getTotalEntreeResto(
			 @RequestParam("startDateTime") 
		        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
		        
		        @RequestParam("endDateTime") 
		        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime
		        ) {

	    BigDecimal total = dashboardService.calculerTotalMontantVenteByInterval(startDateTime, endDateTime);
	    return ResponseEntity.ok(total);
	}
	
	@GetMapping("/total-entrees-reservation")
	public ResponseEntity<BigDecimal> getTotalEntreeReservation(
			    @RequestParam("startDateTime") 
		        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
		        
		        @RequestParam("endDateTime") 
		        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime 
			
			) {

	    BigDecimal total = dashboardService.calculerTotalMontantReservationByInterval(startDateTime, endDateTime);
	    return ResponseEntity.ok(total);
	}
	

}
