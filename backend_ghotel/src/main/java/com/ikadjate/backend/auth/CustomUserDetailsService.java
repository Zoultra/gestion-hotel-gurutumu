package com.ikadjate.backend.auth;

import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

  
    
    public Utilisateur loadUtilisateurByUsername(String username) {
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé, verifier si la session n'est pas fermée"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Utilisateur utilisateur = loadUtilisateurByUsername(username);
        return new CustomUserDetails(utilisateur);
    }
}
