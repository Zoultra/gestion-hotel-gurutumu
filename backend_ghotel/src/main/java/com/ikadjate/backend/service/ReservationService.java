package com.ikadjate.backend.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.dto.ClientDto;
import com.ikadjate.backend.dto.FactureDto;
import com.ikadjate.backend.dto.LigneReservationDto;
import com.ikadjate.backend.dto.ReservationDto;
import com.ikadjate.backend.model.Chambre;
import com.ikadjate.backend.model.Client;
import com.ikadjate.backend.model.Facture;
import com.ikadjate.backend.model.Reservation;
import com.ikadjate.backend.model.LigneReservation;
import com.ikadjate.backend.model.MouvementCaisse;
import com.ikadjate.backend.model.OperationType;
import com.ikadjate.backend.model.Salle;
import com.ikadjate.backend.model.StatutReservation;
import com.ikadjate.backend.model.TypeMouvement;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.model.Vente;
import com.ikadjate.backend.repository.ChambreRepository;
import com.ikadjate.backend.repository.ClientRepository;
import com.ikadjate.backend.repository.FactureRepository;
import com.ikadjate.backend.repository.LigneReservationRepository;
import com.ikadjate.backend.repository.MouvementCaisseRepository;
import com.ikadjate.backend.repository.ReservationRepository;
import com.ikadjate.backend.repository.SalleRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LigneReservationRepository ligneReservationRepository;
    private final ChambreRepository chambreRepository;
    private final SalleRepository salleRepository;
    private final ClientRepository clientRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MouvementCaisseRepository mouvementRepository;
    private final FactureRepository factureRepository;
    
    public ReservationService(
        ReservationRepository reservationRepository,
        LigneReservationRepository ligneReservationRepository,
        ChambreRepository chambreRepository,
        SalleRepository salleRepository,
        ClientRepository clientRepository,
        UtilisateurRepository utilisateurRepository,
        MouvementCaisseRepository mouvementRepository,
        FactureRepository factureRepository) {
        this.reservationRepository = reservationRepository;
        this.ligneReservationRepository = ligneReservationRepository;
        this.chambreRepository = chambreRepository;
        this.salleRepository = salleRepository;
        this.clientRepository = clientRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.mouvementRepository = mouvementRepository;
        this.factureRepository = factureRepository;
    }

    
    @Autowired
    private CaisseService caisseService;
    
    @Autowired
    private FactureService factureService;
    
    @Transactional
    public ReservationDto enregistrerReservation(ReservationDto dto) {
    	
        // Validation initiale
        if (dto.getClient_id() == null) {
            throw new IllegalArgumentException("Client ID obligatoire");
        }
        if (dto.getLignes() == null || dto.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Au moins une ligne requise");
        }

        // Récupération client
        Client client = clientRepository.findById(dto.getClient_id())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        
        //Remise de la facture 
        BigDecimal remise = dto.getMontantRemise() != null ? dto.getMontantRemise() : BigDecimal.ZERO;

        // Création de la réservation avec valeur par défaut pour montantTotal
        Reservation reservation = new Reservation();
        reservation.setCode(dto.getCode());
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setStatut(dto.getStatut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setClient(client);
        reservation.setMontantTotal(BigDecimal.ZERO); // Initialisation obligatoire

        // Sauvegarde initiale de la réservation
        reservation = reservationRepository.save(reservation);

        BigDecimal total = BigDecimal.ZERO;
        List<LigneReservation> lignes = new ArrayList<>();

        // Traitement des lignes
        for (LigneReservationDto ligneDto : dto.getLignes()) {
        	
        	
           
            
            
            if (ligneDto.getIdObjet() == null) {
                throw new IllegalArgumentException("ID chambre/salle manquant");
            }

            Long idObjet = ligneDto.getIdObjet();
            String type = ligneDto.getType(); // "CHAMBRE" ou "SALLE"
            Long conflits = 0L;

            if ("CHAMBRE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsChambreEnConflit(
                    idObjet,
                    reservation.getDateDebut(),
                    reservation.getDateFin()
                );
            } else if ("SALLE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsSalleEnConflit(
                    idObjet,
                    reservation.getDateDebut(),
                    reservation.getDateFin()
                );
            } else {
                throw new IllegalArgumentException("Type d'objet inconnu : " + type);
            }

            if (conflits > 0) {
                throw new IllegalStateException("Cette " + type.toLowerCase() + " est déjà réservée pour ces dates.");
            }
   
            
      
            

            LigneReservation ligne = new LigneReservation();
            ligne.setReservation(reservation);
            
            // Gestion null-safe
            Integer nombreJours = ligneDto.getNombreJours();
            
            ligne.setNombreJours(nombreJours != null ? nombreJours : 0);

            if ("CHAMBRE".equalsIgnoreCase(ligneDto.getType())) {
                Chambre chambre = chambreRepository.findById(ligneDto.getIdObjet())
                        .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
                ligne.setChambre(chambre);
                ligne.setPrixUnitaire(chambre.getTarifParNuit() != null ? chambre.getTarifParNuit() : BigDecimal.ZERO);

                // Ajouter les infos complètes de la chambre dans le DTO
                ligneDto.setChambre(chambre);
            } else if ("SALLE".equalsIgnoreCase(ligneDto.getType())) {
                Salle salle = salleRepository.findById(ligneDto.getIdObjet())
                        .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
                ligne.setSalle(salle);
                ligne.setPrixUnitaire(salle.getTarif() != null ? salle.getTarif() : BigDecimal.ZERO);

                // Ajouter les infos complètes de la salle dans le DTO
                ligneDto.setSalle(salle);
            } else {
                throw new IllegalArgumentException("Type inconnu : " + ligneDto.getType());
            }


            // Calcul du sous-total
            BigDecimal sousTotal = ligne.getPrixUnitaire()
                    .multiply(BigDecimal.valueOf(ligne.getNombreJours()));
            total = total.add(sousTotal);
            lignes.add(ligne);
            
            
          
            
            
        }

        // Sauvegarde des lignes
        ligneReservationRepository.saveAll(lignes);

        // Mise à jour du montant total
        reservation.setMontantTotal(total);
        reservation = reservationRepository.save(reservation);

        // Mise à jour du DTO de retour
        dto.setMontantTotal(total);
        
        dto.setId(reservation.getId()); 
        
        // Générer une facture pour la reservation
        
        factureService.genererFacturePourReservation(reservation.getId(), remise);
        
        // Générer un mouvement d'entrée en caisse
       
      //  caisseService.enregistrerMouvementReservation(reservation);
       

        return dto;
    }
    
    
    
    @Transactional
    public ReservationDto modifierReservation2(Long id, ReservationDto dto) {

        if (dto.getClient_id() == null) {
            throw new IllegalArgumentException("Client ID obligatoire");
        }
        if (dto.getLignes() == null || dto.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Au moins une ligne est requise");
        }

        // Vérifier si la réservation existe
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        // Récupération client
        Client client = clientRepository.findById(dto.getClient_id())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Remise
        BigDecimal remise = dto.getMontantRemise() != null ? dto.getMontantRemise() : BigDecimal.ZERO;

        // Mise à jour des champs
        reservation.setCode(dto.getCode());
        reservation.setDateDebut(dto.getDateDebut());
        reservation.setDateFin(dto.getDateFin());
        reservation.setDateReservation(dto.getDateReservation());
        reservation.setStatut(dto.getStatut());
        reservation.setCode(dto.getCode());
        reservation.setClient(client);

        // Supprimer les anciennes lignes
        ligneReservationRepository.deleteByReservationId(reservation.getId());

        BigDecimal total = BigDecimal.ZERO;
        List<LigneReservation> nouvellesLignes = new ArrayList<>();

        for (LigneReservationDto ligneDto : dto.getLignes()) {
            if (ligneDto.getIdObjet() == null) {
                throw new IllegalArgumentException("ID chambre/salle manquant");
            }

            Long idObjet = ligneDto.getIdObjet();
            String type = ligneDto.getType();

            // Vérification des conflits
            Long conflits = 0L;
            if ("CHAMBRE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsChambreEnConflitExcludingReservation(
                    idObjet, reservation.getDateDebut(), reservation.getDateFin(), reservation.getId());
            } else if ("SALLE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsSalleEnConflitExcludingReservation(
                    idObjet, reservation.getDateDebut(), reservation.getDateFin(), reservation.getId());
            } else {
                throw new IllegalArgumentException("Type d'objet inconnu : " + type);
            }

            if (conflits > 0) {
                throw new IllegalStateException("Cette " + type.toLowerCase() + " est déjà réservée pour ces dates.");
            }

            LigneReservation ligne = new LigneReservation();
            ligne.setReservation(reservation);
            
            Integer nombreJours = ligneDto.getNombreJours();
            
            ligne.setNombreJours(nombreJours != null ? nombreJours : 0);

            if ("CHAMBRE".equalsIgnoreCase(type)) {
                Chambre chambre = chambreRepository.findById(idObjet)
                        .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
                ligne.setChambre(chambre);
                ligne.setPrixUnitaire(chambre.getTarifParNuit() != null ? chambre.getTarifParNuit() : BigDecimal.ZERO);
            } else {
                Salle salle = salleRepository.findById(idObjet)
                        .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
                ligne.setSalle(salle);
                ligne.setPrixUnitaire(salle.getTarif() != null ? salle.getTarif() : BigDecimal.ZERO);
            }

            BigDecimal sousTotal = ligne.getPrixUnitaire()
                    .multiply(BigDecimal.valueOf(ligne.getNombreJours()));
            total = total.add(sousTotal);
            nouvellesLignes.add(ligne);
        }

        // Sauvegarde des nouvelles lignes
        ligneReservationRepository.saveAll(nouvellesLignes);

        // Mise à jour du montant total
        reservation.setMontantTotal(total);
        reservationRepository.save(reservation);

        // Mettre à jour la facture
        
        
         factureService.genererFacturePourReservation(reservation.getId(), remise);

        dto.setId(reservation.getId());
        dto.setMontantTotal(total);

        return dto;
    }

    
    
    
    // Récupérer une réservation par son ID
    public Optional<ReservationDto> getReservationById(Long id) {
        return reservationRepository.findById(id)
            .map(reservation -> {
                // Conversion de l'entité Reservation en DTO
                ReservationDto dto = new ReservationDto();
                dto.setId(reservation.getId());
                dto.setCode(reservation.getCode());
                dto.setDateDebut(reservation.getDateDebut());
                dto.setDateFin(reservation.getDateFin());
                
                dto.setStatut(reservation.getStatut());
                dto.setDateReservation(reservation.getDateReservation());
                dto.setMontantTotal(reservation.getMontantTotal());
                // Ajoute d'autres propriétés selon tes besoins
                
                // Conversion du client
                ClientDto clientDto = new ClientDto();
                clientDto.setId(reservation.getClient().getId());
                clientDto.setNom(reservation.getClient().getNom());
                clientDto.setPrenom(reservation.getClient().getPrenom());
                clientDto.setAdresse(reservation.getClient().getAdresse());
                clientDto.setEmail(reservation.getClient().getEmail());
                clientDto.setTelephone(reservation.getClient().getTelephone());
                clientDto.setObservation(reservation.getClient().getObservation());
                dto.setClient(clientDto);
                
                // Conversion des lignes
                List<LigneReservationDto> ligneDtos = reservation.getLignes().stream().map(ligne -> {
                    LigneReservationDto lDto = new LigneReservationDto();
                    lDto.setId(ligne.getId());
                    lDto.setNombreJours(ligne.getNombreJours());
                    lDto.setPrixUnitaire(ligne.getPrixUnitaire());
                    if (ligne.getChambre() != null) {
                        lDto.setType("CHAMBRE");
                        lDto.setIdObjet(ligne.getChambre().getId());
                        lDto.setChambre(ligne.getChambre());
                    } else if (ligne.getSalle() != null) {
                        lDto.setType("SALLE");
                        lDto.setIdObjet(ligne.getSalle().getId());
                        lDto.setSalle(ligne.getSalle());
                    }
                    return lDto;
                }).collect(Collectors.toList());
                
                
                

                dto.setLignes(ligneDtos);
                return dto;
            });
    }
  
    
 // Méthode de conversion de Reservation en ReservationDto
    private ReservationDto convertToDto(Reservation reservation) {
    	
        ReservationDto dto = new ReservationDto();
        dto.setId(reservation.getId());
        dto.setCode(reservation.getCode());
        dto.setDateDebut(reservation.getDateDebut());
        dto.setDateFin(reservation.getDateFin());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setMontantTotal(reservation.getMontantTotal());
        dto.setStatut(reservation.getStatut());

        // Conversion du client
        ClientDto clientDto = new ClientDto();
        clientDto.setId(reservation.getClient().getId());
        clientDto.setNom(reservation.getClient().getNom());
        clientDto.setPrenom(reservation.getClient().getPrenom());
        clientDto.setAdresse(reservation.getClient().getAdresse());
        clientDto.setEmail(reservation.getClient().getEmail());
        clientDto.setTelephone(reservation.getClient().getTelephone());
        clientDto.setObservation(reservation.getClient().getObservation());
        dto.setClient(clientDto);

        // Conversion des lignes
        List<LigneReservationDto> ligneDtos = reservation.getLignes().stream().map(ligne -> {
            LigneReservationDto lDto = new LigneReservationDto();
            lDto.setId(ligne.getId());
            lDto.setNombreJours(ligne.getNombreJours());
            lDto.setPrixUnitaire(ligne.getPrixUnitaire());
            if (ligne.getChambre() != null) {
                lDto.setType("CHAMBRE");
                lDto.setIdObjet(ligne.getChambre().getId());
                lDto.setChambre(ligne.getChambre());
            } else if (ligne.getSalle() != null) {
                lDto.setType("SALLE");
                lDto.setIdObjet(ligne.getSalle().getId());
                lDto.setSalle(ligne.getSalle());
            }
            return lDto;
        }).collect(Collectors.toList());
        
        if (reservation.getFacture() != null) {
            FactureDto factureDto = new FactureDto();
            factureDto.setId(reservation.getFacture().getId());
            factureDto.setNumero(reservation.getFacture().getNumero());
            factureDto.setMontantTotal(reservation.getFacture().getMontantTotal());
            factureDto.setMontantNet(reservation.getFacture().getMontantNet());
            factureDto.setMontantPaye(reservation.getFacture().getMontantPaye());
            factureDto.setMontantRemise(reservation.getFacture().getMontantRemise());
            factureDto.setResteAPayer(reservation.getFacture().getResteAPayer());
            factureDto.setDateFacture(reservation.getFacture().getDateFacture());
            factureDto.setPaiements(reservation.getFacture().getPaiements());
            // autres mappings utiles
            dto.setFacture(factureDto);
        }
        

        dto.setLignes(ligneDtos);

        return dto;
    }

    // ALL DATA METHODE
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
   // Récupérer toutes les réservations DTO METHODE 
    public List<ReservationDto> getAllReservationsDto() {
        List<Reservation> reservations = reservationRepository.findAllByOrderByIdDesc();
        return reservations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public boolean deleteReservation(Long id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        
        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            
            // 1. Supprimer d'abord le mouvement de caisse associé
            mouvementRepository.deleteByReservationId(reservation.getId());
            
            // 2. Supprimer la réservation
            reservationRepository.delete(reservation);
            
            return true;
        }
        return false;
    }
    
    
    
    // Partie modif after coding
    
    
    
    @Transactional
    public ReservationDto modifierReservation(Long id, ReservationDto dto) {
        // Validation initiale
        if (dto.getClient_id() == null) {
            throw new IllegalArgumentException("Client ID obligatoire");
        }
        if (dto.getLignes() == null || dto.getLignes().isEmpty()) {
            throw new IllegalArgumentException("Au moins une ligne requise");
        }

        // 1. Récupérer la réservation existante
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        // 2. Supprimer les anciennes lignes
        ligneReservationRepository.deleteByReservationId(id);
        reservationRepository.flush(); // Force la suppression immédiate

        // Récupération client
        Client client = clientRepository.findById(dto.getClient_id())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // 3. Mettre à jour la réservation existante (même ID)
        existingReservation.setDateDebut(dto.getDateDebut());
        existingReservation.setDateFin(dto.getDateFin());
        existingReservation.setStatut(dto.getStatut());
        existingReservation.setClient(client);
        

        BigDecimal total = BigDecimal.ZERO;
        
        List<LigneReservation> nouvellesLignes = new ArrayList<>();

        // Traitement des nouvelles lignes
        for (LigneReservationDto ligneDto : dto.getLignes()) {
            if (ligneDto.getIdObjet() == null) {
                throw new IllegalArgumentException("ID chambre/salle manquant");
            }

            Long idObjet = ligneDto.getIdObjet();
            String type = ligneDto.getType();
            Long conflits = 0L;

            // Vérification des conflits (en excluant la réservation actuelle)
            if ("CHAMBRE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsChambreEnConflitExcludingReservation(
                    idObjet,
                    existingReservation.getDateDebut(),
                    existingReservation.getDateFin(),
                    id
                );
            } else if ("SALLE".equalsIgnoreCase(type)) {
                conflits = ligneReservationRepository.countReservationsSalleEnConflitExcludingReservation(
                    idObjet,
                    existingReservation.getDateDebut(),
                    existingReservation.getDateFin(),
                    id
                );
            } else {
                throw new IllegalArgumentException("Type d'objet inconnu : " + type);
            }

            if (conflits > 0) {
                throw new IllegalStateException("Cette " + type + " est déjà réservée pour ces dates.");
            }

            LigneReservation ligne = new LigneReservation();
            ligne.setReservation(existingReservation);
            
            Integer nombreJours = ligneDto.getNombreJours();
            
            ligne.setNombreJours(nombreJours != null ? nombreJours : 0);
            
            if ("CHAMBRE".equalsIgnoreCase(type)) {
                Chambre chambre = chambreRepository.findById(ligneDto.getIdObjet())
                        .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));
                ligne.setChambre(chambre);
                ligne.setPrixUnitaire(chambre.getTarifParNuit() != null ? chambre.getTarifParNuit() : BigDecimal.ZERO);
            } else if ("SALLE".equalsIgnoreCase(type)) {
                Salle salle = salleRepository.findById(ligneDto.getIdObjet())
                        .orElseThrow(() -> new RuntimeException("Salle non trouvée"));
                ligne.setSalle(salle);
                ligne.setPrixUnitaire(salle.getTarif() != null ? salle.getTarif() : BigDecimal.ZERO);
            }

            BigDecimal sousTotal = ligne.getPrixUnitaire()
                    .multiply(BigDecimal.valueOf(ligne.getNombreJours()));
            total = total.add(sousTotal);
            nouvellesLignes.add(ligne);
        }

        // Sauvegarde des nouvelles lignes
        ligneReservationRepository.saveAll(nouvellesLignes);

        // Mise à jour du montant total
        existingReservation.setMontantTotal(total);
        Reservation updatedReservation = reservationRepository.save(existingReservation);

        // 12. Mise à jour du montant total
        BigDecimal ancienMontant = existingReservation.getMontantTotal();
     
       
            // Récupérer l'ancien mouvement
        	 MouvementCaisse ancienMouvement = mouvementRepository.findByReservationId(updatedReservation.getId())
                     .stream()
                     .findFirst()
                     .orElse(null);
      
            
                // Supprimer l'ancien mouvement
            	mouvementRepository.delete(ancienMouvement);   
            
            // Créer un nouveau mouvement si montant > 0 
               
                caisseService.enregistrerMouvementReservation(updatedReservation);
                 

        // Mise à jour du DTO de retour
        dto.setMontantTotal(total);
        dto.setId(updatedReservation.getId());
        
        return dto;
    }
    
    
    
}
