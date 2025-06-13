
package com.ikadjate.backend.service;

import org.springframework.stereotype.Service;

import com.ikadjate.backend.model.Role;
import com.ikadjate.backend.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Récupérer tous les rôles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Récupérer un rôle par son ID
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    // Sauvegarder un rôle
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
    
    public Role updateRole(Long id, Role roleDetails) {
        Role existingRole = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Le Role selectionné " + id + " n'existe pas "));
        
        existingRole.setNomrole(roleDetails.getNomrole());
        
        return roleRepository.save(existingRole);
    }

    // Supprimer un rôle
    public String deleteRole(Long id) {
        roleRepository.deleteById(id);
        return "Le rôle avec l'ID " + id + " a été supprimé avec succès.";
    }
}

