package com.ikadjate.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.model.Role;
import com.ikadjate.backend.service.RoleService;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ROLE)
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Récupérer tous les rôles
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // Récupérer un rôle par son ID
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    // Créer un nouveau rôle
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }
    
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        return roleService.updateRole(id, roleDetails);
    }

    // Supprimer un rôle
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        String message = roleService.deleteRole(id);
        return ResponseEntity.ok(message);
    }
}
