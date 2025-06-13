package com.ikadjate.backend.controller;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.dto.ReservationDto;
import com.ikadjate.backend.dto.VenteDto;
import com.ikadjate.backend.model.Article;
import com.ikadjate.backend.model.Reservation;
import com.ikadjate.backend.model.Vente;
import com.ikadjate.backend.service.ReservationService;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(ApiPaths.RESERVATION)
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Endpoint pour enregistrer une réservation
   // @PostMapping
   // public ResponseEntity<ReservationDto> enregistrerReservation(@RequestBody ReservationDto reservationDto) {
   //     try {
   //         ReservationDto createdReservation = reservationService.enregistrerReservation(reservationDto);
    //        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
   //     } catch (RuntimeException e) {
   //         return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
   //     }
  //  }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Le corps de la commande est vide !");
        }
        return ResponseEntity.ok(reservationService.enregistrerReservation(dto));
    }
    // Endpoint pour obtenir les détails d'une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable("id") Long id) {
        // Tu peux ajouter un service pour récupérer une réservation par ID et la convertir en DTO
        // Si elle n'existe pas, tu peux renvoyer une erreur
        return reservationService.getReservationById(id)
                .map(reservationDto -> new ResponseEntity<>(reservationDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // Récupérer toutes les réservations
    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservationsDto() {
        List<ReservationDto> reservations = reservationService.getAllReservationsDto();
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne un statut 204 si aucune réservation
        } else {
            return ResponseEntity.ok(reservations); // Retourne un statut 200 avec la liste des réservations
        }
    }
    
    
    // Récupérer toutes les réservations
    @GetMapping("/liste")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne un statut 204 si aucune réservation
        } else {
            return ResponseEntity.ok(reservations); // Retourne un statut 200 avec la liste des réservations
        }
    }
    
     

    

    // Endpoint pour supprimer une réservation (optionnel)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        // Tu peux ajouter un service pour supprimer la réservation par ID
        boolean isDeleted = reservationService.deleteReservation(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,
            @Validated @RequestBody ReservationDto reservationDto) {
        
            ReservationDto updatedReservation = reservationService.modifierReservation2(id, reservationDto);
            return ResponseEntity.ok(updatedReservation);   
         
    }

}
