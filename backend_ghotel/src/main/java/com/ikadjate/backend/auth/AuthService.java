package com.ikadjate.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ikadjate.backend.model.Utilisateur;

@Service
public class AuthService {
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public AuthService(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur connecté");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUtilisateur();
        }

        // Si c'est juste le username qui est stocké dans le principal
        String username = principal.toString();
        return userDetailsService.loadUtilisateurByUsername(username);
    }
}
