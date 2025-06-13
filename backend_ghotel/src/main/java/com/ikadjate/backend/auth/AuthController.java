package com.ikadjate.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.ikadjate.backend.config.ApiPaths;
import com.ikadjate.backend.model.Utilisateur;

@RestController
@RequestMapping(ApiPaths.AUTH)
@RequiredArgsConstructor
public class AuthController { 

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());
        Utilisateur utilisateur = userDetails.getUtilisateur();

        String token = jwtUtil.generateToken(utilisateur.getUsername(), utilisateur.getNom(), utilisateur.getPrenom(), utilisateur.getRole());
        
        return new AuthResponse(token);
    }
}
