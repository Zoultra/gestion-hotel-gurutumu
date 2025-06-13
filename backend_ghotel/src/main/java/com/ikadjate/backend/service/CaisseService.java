package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.auth.AuthService;
import com.ikadjate.backend.auth.CustomUserDetailsService;
import com.ikadjate.backend.dto.CaisseJourDto;
import com.ikadjate.backend.dto.MouvementDto;
import com.ikadjate.backend.dto.RecapDayDto;
import com.ikadjate.backend.dto.RecapJournalierDto;
import com.ikadjate.backend.model.Caisse;
import com.ikadjate.backend.model.Depense;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.model.OperationType;
import com.ikadjate.backend.model.Paiement;
import com.ikadjate.backend.model.Reservation;
import com.ikadjate.backend.model.TypeMouvement;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.model.Vente;
import com.ikadjate.backend.repository.CaisseRepository;
import com.ikadjate.backend.repository.ChambreRepository;
import com.ikadjate.backend.repository.MouvementCaisseRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaisseService {

	 
	 
    @Autowired
    private MouvementCaisseRepository mouvementRepository;
    
    @Autowired
    private CaisseRepository caisseRepository;
    
    private final UtilisateurRepository utilisateurRepository;
    
    private final CustomUserDetailsService userDetailsService;
    
    private final AuthenticationManager authenticationManager;
    
    private final AuthService authService;
    

    public MouvementCaisse enregistrerMouvement(MouvementCaisse mouvement) {
    	
    	Caisse caisse = caisseRepository.findByOuverteTrue()
        	    .orElseThrow(() -> new RuntimeException("Aucune caisse ouverte. Veuillez ouvrir la caisse d'abord."));
        	mouvement.setCaisse(caisse);
        	
        mouvement.setDate(LocalDate.now());
        return mouvementRepository.save(mouvement);
    }

    public List<MouvementCaisse> getAllMouvements() {
        return mouvementRepository.findAll();
    }
    
    
    
    public RecapJournalierDto getRecapParDate(LocalDate date) {
        // Récupération des mouvements du jour
        List<MouvementCaisse> mouvementsDuJour = mouvementRepository.findByDate(date);

        BigDecimal totalEntree = mouvementsDuJour.stream()
                .filter(m -> m.getType() == TypeMouvement.ENTREE)
                .map(MouvementCaisse::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSortie = mouvementsDuJour.stream()
                .filter(m -> m.getType() == TypeMouvement.SORTIE)
                .map(MouvementCaisse::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Solde de départ = solde jusqu’à la veille
        BigDecimal soldeDepart = mouvementRepository.calculerSoldeJusquaDate(date.minusDays(1));

        BigDecimal soldeFinal = soldeDepart.add(totalEntree).subtract(totalSortie);

        List<MouvementDto> mouvementsDto = mouvementsDuJour.stream()
        	    .map((MouvementCaisse m) -> MouvementDto.builder()  // Type explicite 'Mouvement'
        	        .id(m.getId())
        	        .description(m.getDescription())
        	        .type(m.getType().toString())
        	        .montant(m.getMontant())
        	        .date(m.getDate())
        	        .build())
        	    .toList();
        
        return RecapJournalierDto.builder()
                .dateOuverture(date)
                .soldeDepart(soldeDepart)
                .totalEntree(totalEntree)
                .totalSortie(totalSortie)
                .soldeFinal(soldeFinal)
                .mouvements(mouvementsDto)
                .build();
    }
    
    
    public Caisse ouvrirCaisse(BigDecimal soldeInitial) {
        Optional<Caisse> caisseExistante = caisseRepository.findByOuverteTrue();
        if (caisseExistante.isPresent()) {
            throw new IllegalStateException("Une caisse est déjà ouverte.");
        }
        
        // Selectionner le user  
        
        Utilisateur utilisateurActif = authService.getCurrentUser();
        Caisse caisse = new Caisse();
        caisse.setDateOuverture(LocalDate.now());
        caisse.setSoldeInitial(soldeInitial);
        caisse.setOuverte(true);
        caisse.setUtilisateur(utilisateurActif);
        return caisseRepository.save(caisse);
    }
    
    
    
    @Transactional
    public Caisse fermerCaisse(Long id, Caisse caisseRequest) {
        // 1. Vérifier que la caisse existe et est ouverte
        Caisse caisse = caisseRepository.findByIdAndOuverteTrue(id)
            .orElseThrow(() -> new IllegalStateException("Aucune caisse ouverte avec l'ID: " + id));

        // 2. Valider que le soldeFinal a été saisi
        if (caisseRequest.getSoldeFinal() == null) {
            throw new IllegalArgumentException("Le solde final est obligatoire pour fermer la caisse");
        }

        // 3. Calculer les totaux pour vérification (optionnel)
      //  List<MouvementCaisse> mouvements = mouvementRepository.findByCaisseId(id);
        
        List<MouvementCaisse> mouvements = mouvementRepository.findByCaisse(caisse);

        
        BigDecimal totalEntree = mouvements.stream()
                .filter(m -> TypeMouvement.ENTREE.equals(m.getType()))
                .map(MouvementCaisse::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSortie = mouvements.stream()
                .filter(m -> TypeMouvement.SORTIE.equals(m.getType()))
                .map(MouvementCaisse::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal soldeFinalTheorique = caisse.getSoldeInitial().add(totalEntree).subtract(totalSortie);


        // 5. Mettre à jour et fermer la caisse
        caisse.setSoldeFinalTheorique(soldeFinalTheorique);
        caisse.setSoldeFinal(caisseRequest.getSoldeFinal());
        caisse.setDateFermeture(LocalDate.now());
        caisse.setOuverte(false);
        
        caisse.fermer();

        return caisseRepository.save(caisse);
    }
    
    public List<Caisse> getAllCaisses() {
        return caisseRepository.findAll();
    }
    
    
    
    
    
   



    public RecapJournalierDto getOperationsJournalierEParDate(LocalDate date) {
    	
        List<Caisse> caisses = caisseRepository.findByDateOuvertureOrFermeture(date);

        if (caisses.isEmpty()) {
            throw new RuntimeException("Aucune activité de caisse pour cette date.");
        }

        // Trouver la première caisse ouverte (par heureOuverture non null)
        Optional<Caisse> premiereCaisse = caisses.stream()
            .filter(c -> c.getHeureOuverture() != null)
            .min(Comparator.comparing(Caisse::getHeureOuverture));

        BigDecimal soldeDepart = premiereCaisse.map(Caisse::getSoldeInitial).orElse(BigDecimal.ZERO);
        
        Optional<Caisse> derniereCaisse = caisses.stream()
        	    .filter(c -> c.getHeureFermeture() != null)
        	    .max(Comparator.comparing(Caisse::getHeureFermeture));

        BigDecimal soldeFinal = derniereCaisse.map(Caisse::getSoldeFinal).orElse(BigDecimal.ZERO);

        BigDecimal totalEntree = BigDecimal.ZERO;
        BigDecimal totalSortie = BigDecimal.ZERO;
       

        List<MouvementDto> mouvementsDto = new ArrayList<>();

        for (Caisse caisse : caisses) {
            List<MouvementCaisse> mouvements = mouvementRepository
                .findByCaisseAndDate(caisse, date);

            for (MouvementCaisse m : mouvements) {
                if (m.getType() == TypeMouvement.ENTREE) {
                    totalEntree = totalEntree.add(m.getMontant());
                } else {
                    totalSortie = totalSortie.add(m.getMontant());
                }

                mouvementsDto.add(MouvementDto.builder()
                    .id(m.getId())
                    .description(m.getDescription())
                    .type(m.getType().toString())
                    .montant(m.getMontant())
                    .date(m.getDate())
                    .build());
            }
        }

        BigDecimal soldeFinalTheorique = soldeDepart.add(totalEntree).subtract(totalSortie);

        return RecapJournalierDto.builder()
            .dateOuverture(date)
            .soldeDepart(soldeDepart)
            .totalEntree(totalEntree)
            .totalSortie(totalSortie)
            .soldeFinalTheorique(soldeFinalTheorique)
            .soldeFinal(soldeFinal)
            .mouvements(mouvementsDto)
            .build();
    }
    
    
    // RECAP JOURNALIER ENTRE LES DATES 
    
    public List<RecapJournalierDto> getOperationsEntreDates(LocalDate startDate, LocalDate endDate) {
        List<Caisse> caisses = caisseRepository.findCaissesActivesDansIntervalle(startDate, endDate);

        if (caisses.isEmpty()) {
            throw new RuntimeException("Aucune activité de caisse entre les dates spécifiées.");
        }

        List<RecapJournalierDto> recapList = new ArrayList<>();

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDate currentDate = current; // Effectively final

            List<Caisse> caissesDuJour = caisses.stream()
                .filter(c -> currentDate.equals(c.getDateOuverture()) || currentDate.equals(c.getDateFermeture()))
                .collect(Collectors.toList());

            if (!caissesDuJour.isEmpty()) {
                Optional<Caisse> premiereCaisse = caissesDuJour.stream()
                    .filter(c -> c.getHeureOuverture() != null)
                    .min(Comparator.comparing(Caisse::getHeureOuverture));

                Optional<Caisse> derniereCaisse = caissesDuJour.stream()
                    .filter(c -> c.getHeureFermeture() != null)
                    .max(Comparator.comparing(Caisse::getHeureFermeture));

                BigDecimal soldeDepart = premiereCaisse.map(Caisse::getSoldeInitial).orElse(BigDecimal.ZERO);
                BigDecimal soldeFinal = derniereCaisse.map(Caisse::getSoldeFinal).orElse(BigDecimal.ZERO);

                BigDecimal totalEntree = BigDecimal.ZERO;
                BigDecimal totalSortie = BigDecimal.ZERO;
                List<MouvementDto> mouvementsDto = new ArrayList<>();

                for (Caisse caisse : caissesDuJour) {
                    List<MouvementCaisse> mouvements = mouvementRepository.findByCaisseAndDate(caisse, currentDate);

                    for (MouvementCaisse m : mouvements) {
                        if (m.getType() == TypeMouvement.ENTREE) {
                            totalEntree = totalEntree.add(m.getMontant());
                        } else {
                            totalSortie = totalSortie.add(m.getMontant());
                        }

                        mouvementsDto.add(MouvementDto.builder()
                            .id(m.getId())
                            .description(m.getDescription())
                            .type(m.getType().toString())
                            .montant(m.getMontant())
                            .date(m.getDate())
                            .build());
                    }
                }

                BigDecimal soldeFinalTheorique = soldeDepart.add(totalEntree).subtract(totalSortie);

                recapList.add(RecapJournalierDto.builder()
                    .dateOuverture(currentDate)
                    .soldeDepart(soldeDepart)
                    .totalEntree(totalEntree)
                    .totalSortie(totalSortie)
                    .soldeFinalTheorique(soldeFinalTheorique)
                    .soldeFinal(soldeFinal)
                    .mouvements(mouvementsDto)
                    .build());
            }

            current = current.plusDays(1);
        }

        return recapList;
    }

    
    
    // ENTRE 07 ET 03 
    
    
    public List<RecapJournalierDto> getOperationsEntreDatesIn0703(LocalDate startDate, LocalDate endDate) {
    	  // Étape 1 : Convertir les bornes en LocalDateTime
        LocalDateTime startDateTime = startDate.atTime(7, 0);  // 09/06/2025 07:00
        LocalDateTime endDateTime = endDate.plusDays(1).atTime(3, 0);  // 11/06/2025 03:00

        // Étape 2 : Charger toutes les caisses ouvertes ou fermées dans l'intervalle de dates brutes
        List<Caisse> caissesBrutes = caisseRepository.findAll();

        // Étape 3 : Filtrer les caisses dans l'intervalle réel
        List<Caisse> caisses = caissesBrutes.stream()
            .filter(c -> {
                LocalDateTime ouverture = c.getDateOuverture().atTime(c.getHeureOuverture());
                LocalDateTime fermeture = c.getDateFermeture() != null && c.getHeureFermeture() != null
                    ? c.getDateFermeture().atTime(c.getHeureFermeture())
                    : ouverture.plusHours(1); // valeur par défaut si non fermée

                return !ouverture.isAfter(endDateTime) && !fermeture.isBefore(startDateTime);
            })
            .collect(Collectors.toList());

        if (caisses.isEmpty()) {
            throw new RuntimeException("Aucune activité de caisse entre les dates spécifiées.");
        }

        List<RecapJournalierDto> recapList = new ArrayList<>();

        for (Caisse caisse : caisses) {
            List<MouvementCaisse> mouvements = mouvementRepository.findByCaisseAndDateTimeRange(caisse, startDate, endDate);

            BigDecimal soldeDepart = caisse.getSoldeInitial() != null ? caisse.getSoldeInitial() : BigDecimal.ZERO;
            BigDecimal soldeFinal = caisse.getSoldeFinal() != null ? caisse.getSoldeFinal() : BigDecimal.ZERO;
            BigDecimal totalEntree = BigDecimal.ZERO;
            BigDecimal totalSortie = BigDecimal.ZERO;
            List<MouvementDto> mouvementsDto = new ArrayList<>();

            for (MouvementCaisse m : mouvements) {
                if (m.getType() == TypeMouvement.ENTREE) {
                    totalEntree = totalEntree.add(m.getMontant());
                } else {
                    totalSortie = totalSortie.add(m.getMontant());
                }

                mouvementsDto.add(MouvementDto.builder()
                    .id(m.getId())
                    .description(m.getDescription())
                    .type(m.getType().toString())
                    .montant(m.getMontant())
                    .date(m.getDate())
                    .build());
            }

            BigDecimal soldeFinalTheorique = soldeDepart.add(totalEntree).subtract(totalSortie);

            recapList.add(RecapJournalierDto.builder()
                .dateOuverture(caisse.getDateOuverture())
                .heureOuverture(caisse.getHeureOuverture()) 
                .dateFermeture(caisse.getDateFermeture())
                .heureFermeture(caisse.getHeureFermeture()) 
                .soldeDepart(soldeDepart)
                .totalEntree(totalEntree)
                .totalSortie(totalSortie)
                .soldeFinalTheorique(soldeFinalTheorique)
                .soldeFinal(soldeFinal)
                .mouvements(mouvementsDto)
                .build());
        }

        return recapList;
    }


    
    
    
    public RecapDayDto getRecapJournalier(LocalDate date) {
        // Récupérer toutes les caisses ouvertes ou fermées à cette date
        List<Caisse> caissesDuJour = caisseRepository.findByDateOuvertureOrFermeture(date);

        if (caissesDuJour.isEmpty()) {
            throw new RuntimeException("Aucune activité de caisse pour cette date.");
        }

        List<CaisseJourDto> caisseJourDtos = new ArrayList<>();

        for (Caisse caisse : caissesDuJour) {
            List<MouvementCaisse> mouvements = mouvementRepository.findByCaisseAndDate(caisse, date);

            BigDecimal totalEntree = BigDecimal.ZERO;
            BigDecimal totalSortie = BigDecimal.ZERO;

            List<MouvementDto> mouvementsDto = new ArrayList<>();
            for (MouvementCaisse m : mouvements) {
                if (m.getType() == TypeMouvement.ENTREE) {
                    totalEntree = totalEntree.add(m.getMontant());
                } else {
                    totalSortie = totalSortie.add(m.getMontant());
                }

                mouvementsDto.add(MouvementDto.builder()
                        .id(m.getId())
                        .description(m.getDescription())
                        .type(m.getType().name())
                        .montant(m.getMontant())
                        .date(m.getDate())
                        .build());
            }

            BigDecimal benefice = caisse.getSoldeInitial().add(totalEntree).subtract(totalSortie);

            caisseJourDtos.add(CaisseJourDto.builder()
                    .caisseId(caisse.getId())
                    .date_ouverture(caisse.getDateOuverture())
                    .date_fermeture(caisse.getDateFermeture())
                    .soldeInitial(caisse.getSoldeInitial())
                    .totalEntree(totalEntree)
                    .totalSortie(totalSortie)
                    .soldeFinal(caisse.getSoldeFinal())
                    .ouverte(caisse.getOuverte())
                    .utilisateur(caisse.getUtilisateur())
                    
                    .mouvements(mouvementsDto)
                    .build());
        }

        return RecapDayDto.builder()
                .date(date)
                .caisses(caisseJourDtos)
                .build();
    }
    
    
    
    
   
    
    // Enregistrement Mouvement pour la reservation
    
   
    public void enregistrerMouvementReservation(Reservation reservation) {
    	 
    	MouvementCaisse mouvement = new MouvementCaisse();
    	 
    	Caisse caisse = caisseRepository.findByOuverteTrue()
        	    .orElseThrow(() -> new RuntimeException("Aucune caisse ouverte. Veuillez ouvrir la caisse d'abord."));
        	
        
        	// Selectionner le user  
        	
        Utilisateur utilisateur = authService.getCurrentUser();
        mouvement.setCaisse(caisse);	
        mouvement.setDate(LocalDate.now());
        mouvement.setType(TypeMouvement.ENTREE);
        mouvement.setMontant(reservation.getMontantTotal());
        mouvement.setDescription("RESERVATION CHAMBRE/SALLE" + reservation.getCode());
        mouvement.setReservationId(reservation.getId());
        mouvement.setOperationType(OperationType.RESERVATION);
        mouvement.setUtilisateur(utilisateur); // si disponible
        mouvementRepository.save(mouvement);
    }
    
    
 // Enregistrement Mouvement pour le paiement
    
    
    public void enregistrerMouvementPaiement(Paiement paiement) {
    	 
    	MouvementCaisse mouvement = new MouvementCaisse();
    	 
    	Caisse caisse = caisseRepository.findByOuverteTrue()
        	    .orElseThrow(() -> new RuntimeException("Aucune caisse ouverte. Veuillez ouvrir la caisse d'abord."));
        	
    	  System.out.println("Appel de enregistrerMouvementPaiement");
        	// Selectionner le user  
        	
        Utilisateur utilisateur = authService.getCurrentUser();
        mouvement.setCaisse(caisse);	
        mouvement.setDate(LocalDate.now());
        mouvement.setType(TypeMouvement.ENTREE);
        mouvement.setMontant(paiement.getMontant());
        mouvement.setDescription("Paiement de facture de reservation");
        mouvement.setPaiementId(paiement.getId());
        mouvement.setOperationType(OperationType.RESERVATION);
        mouvement.setUtilisateur(utilisateur); // si disponible
        mouvementRepository.save(mouvement);
    }
    
    public MouvementCaisse findByOperationIdAndType(Long operationId, OperationType operationType) {
        return mouvementRepository.findByReservationId(operationId)
                .stream()
                .findFirst()
                .orElse(null);
    }
    
    
    @Transactional
    public void enregistrerMouvementVente(Vente vente) {
        try {
            // 1. Vérification de l'utilisateur courant
            Utilisateur utilisateur;
            try {
                utilisateur = authService.getCurrentUser();
                if (utilisateur == null) {
                    throw new UsernameNotFoundException("Aucun utilisateur authentifié");
                }
            } catch (UsernameNotFoundException e) {
                     throw new SecurityException("Session expirée ou invalide. Veuillez vous reconnecter.");
            }

            // 2. Vérification de la caisse ouverte
            Caisse caisse = caisseRepository.findByOuverteTrue()
                    .orElseThrow(() -> new IllegalStateException("Aucune caisse ouverte. Veuillez ouvrir une caisse avant toute opération."));

            // 3. Validation de la vente
            if (vente == null || vente.getId() == null) {
                throw new IllegalArgumentException("Vente invalide - ID manquant");
            }

            // 4. Création du mouvement
            MouvementCaisse mouvement = new MouvementCaisse();
            mouvement.setCaisse(caisse);
            mouvement.setDate(LocalDate.now());
            mouvement.setType(TypeMouvement.ENTREE);
            mouvement.setMontant(vente.getMontantTotal());
            mouvement.setDescription("VENTE RESTAURANT#" + vente.getId());
            mouvement.setVenteId(vente.getId());
            mouvement.setOperationType(OperationType.VENTE);
            mouvement.setUtilisateur(utilisateur);

            mouvementRepository.save(mouvement);
             
        } catch (Exception e) {
               throw e;
        }
    }
    
 public void enregistrerMouvementDepense(Depense depense) {
    	
    	Utilisateur utilisateur = authService.getCurrentUser();
        
        MouvementCaisse mouvement = new MouvementCaisse();
        Caisse caisse = caisseRepository.findByOuverteTrue()
        	    .orElseThrow(() -> new RuntimeException("Aucune caisse ouverte. Veuillez ouvrir la caisse d'abord."));
        mouvement.setCaisse(caisse);	
        mouvement.setDate(depense.getDateDepense());
        mouvement.setType(TypeMouvement.SORTIE);
        mouvement.setMontant(depense.getMontant());
        mouvement.setDescription("DEPENSE #" + depense.getLibelle());
        mouvement.setDepenseId(depense.getId());
        mouvement.setOperationType(OperationType.DEPENSE);
        mouvement.setUtilisateur(utilisateur);
        mouvementRepository.save(mouvement);
    }
 
 public List<MouvementCaisse> findByVenteId(Long venteId) {
	    return mouvementRepository.findByVenteId(venteId);
	}

    
    
    }

    
    


