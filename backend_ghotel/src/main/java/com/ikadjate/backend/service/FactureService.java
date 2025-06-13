package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.model.Facture;
import com.ikadjate.backend.model.Reservation;
import com.ikadjate.backend.model.StatutFacture;
import com.ikadjate.backend.repository.FactureRepository;
import com.ikadjate.backend.repository.ReservationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FactureService {

    private final FactureRepository factureRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Facture genererFacturePourReservation(Long reservationId, BigDecimal remise) {

        // Récupérer la réservation
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Réservation introuvable avec ID : " + reservationId));
       
        Optional<Facture> factureExistanteOpt = factureRepository.findByReservationId(reservationId);

        
        if (factureExistanteOpt.isPresent()) {
            Facture ancienneFacture = factureExistanteOpt.get();

            // Vérifier s'il y a des paiements
            if (!ancienneFacture.getPaiements().isEmpty()) {
                throw new IllegalStateException("Modification impossible  car un paiement a déjà été enregistré.");
            }

            // Casser la relation bidirectionnelle avant suppression
          
            
            reservation.setFacture(null);
            reservationRepository.save(reservation);

            // Supprimer la facture
            factureRepository.delete(ancienneFacture);
            factureRepository.flush();
            
        }

        
     // Initialisation des montants
        BigDecimal montantTotal = reservation.getMontantTotal();
       // BigDecimal remise = montantRemise != null ? montantRemise : BigDecimal.ZERO;
        BigDecimal montantRemise = remise != null ? remise : BigDecimal.ZERO;
        BigDecimal montantNet = montantTotal.subtract(remise);

        // Création de la facture
        Facture facture = new Facture();
        facture.setNumero(genererNumeroFacture()); // Implémenter un générateur
        facture.setDateFacture(LocalDate.now());
        facture.setMontantTotal(montantTotal);
        facture.setMontantRemise(remise);
        facture.setMontantNet(montantNet);
        facture.setMontantPaye(BigDecimal.ZERO);
        facture.setResteAPayer(montantNet);
        facture.setStatut(StatutFacture.EN_ATTENTE);

        // Lier la réservation et le client
        facture.setReservation(reservation);
        facture.setClient(reservation.getClient());

        return factureRepository.save(facture);
    }

    private String genererNumeroFacture() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = factureRepository.countByDateFacture(LocalDate.now()) + 1;
        return "FACT-" + datePart + "-" + String.format("%03d", countToday);
    }
}
