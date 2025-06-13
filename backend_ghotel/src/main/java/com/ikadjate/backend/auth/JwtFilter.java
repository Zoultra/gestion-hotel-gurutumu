package com.ikadjate.backend.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
 
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilter {
	
	 private static final long serialVersionUID = 1L;

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    
            throws IOException, ServletException {
    	
    	System.out.println("➡️ JwtFilter en cours d'exécution...");
    	
        HttpServletRequest request = (HttpServletRequest) req;
        String authHeader = request.getHeader("Authorization");

      
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                String username = jwtUtil.extractUsername(jwt);
                String role = (String) jwtUtil.extractAllClaims(jwt).get("role");

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                      // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Construire manuellement les autorités à partir du rôle
                    var authorities = java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role));

                    UsernamePasswordAuthenticationToken token =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(token);
                    
                    
                }
            } catch (Exception e) {
                e.printStackTrace();  
            }
        }

        chain.doFilter(req, res);
    }
}
