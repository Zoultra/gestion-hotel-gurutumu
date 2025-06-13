package com.ikadjate.backend.auth;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Ajouté ici
                .authorizeHttpRequests(auth -> auth
                		 
                		 
                        .requestMatchers("/ghotel-api/auth/**").permitAll()
                       // .requestMatchers("/ghotel-api/auth/login/**").permitAll()
                        .requestMatchers("/ghotel-api/utilisateurs").permitAll()
                        .requestMatchers("/ghotel-api/salles/**").permitAll()
                        .requestMatchers("/ghotel-api/roles/**").permitAll()
                        .requestMatchers("/ghotel-api/depenses/**").permitAll()
                        .requestMatchers("/ghotel-api/chambres/**").permitAll()
                        .requestMatchers("/ghotel-api/commande-client/**").permitAll()
                        .requestMatchers("/ghotel-api/articles/**").permitAll()
                        .requestMatchers("/ghotel-api/images/**").permitAll()
                        .requestMatchers("/ghotel-api/ventes/**").permitAll()
                        .requestMatchers("/ghotel-api/reservations/**").permitAll()
                        .requestMatchers("/ghotel-api/dashboard/**").permitAll()
                        .requestMatchers("/ghotel-api/caisse/**").permitAll()
                        .requestMatchers("/ghotel-api/paiement/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    
    
    
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // ou .addAllowedOrigin("*")
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // important si tu utilises les cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    
}
