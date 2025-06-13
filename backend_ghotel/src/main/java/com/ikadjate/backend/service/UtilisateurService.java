package com.ikadjate.backend.service;

 
import com.ikadjate.backend.model.Role;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.dto.UtilisateurDto;
import com.ikadjate.backend.exceptions.ResourceNotFoundException;
import com.ikadjate.backend.repository.RoleRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RoleRepository roleRepository;

    // Créer un utilisateur
    public Utilisateur createUtilisateur(UtilisateurDto request) {
        // Trouver le rôle en BDD
        Role role = roleRepository.findById(request.getRole_id())
                .orElseThrow(() -> new RuntimeException("Role non trouvé"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setUsername(request.getUsername());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole(role);

        return utilisateurRepository.save(utilisateur);
    }


    
    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Récupérer un utilisateur par ID
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));
    }

    // Mettre à jour un utilisateur
    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateurDetails) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id));

        utilisateur.setNom(utilisateurDetails.getNom());
        utilisateur.setPrenom(utilisateurDetails.getPrenom());
        utilisateur.setUsername(utilisateurDetails.getUsername());
        utilisateur.setTelephone(utilisateurDetails.getTelephone());
        utilisateur.setEmail(utilisateurDetails.getEmail());
        utilisateur.setRole(utilisateurDetails.getRole());

        

        return utilisateurRepository.save(utilisateur);
    }

    // Supprimer un utilisateur
    public void deleteUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + id);
        }
        utilisateurRepository.deleteById(id);
    }
}
