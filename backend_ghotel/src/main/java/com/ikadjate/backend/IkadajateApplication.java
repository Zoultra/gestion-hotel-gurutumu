package com.ikadjate.backend;
 
 
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ikadjate.backend.model.Role;
import com.ikadjate.backend.model.Utilisateur;
import com.ikadjate.backend.repository.RoleRepository;
import com.ikadjate.backend.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
 

@SpringBootApplication
@EnableJpaAuditing
public class IkadajateApplication {

	public static void main(String[] args) {
		SpringApplication.run(IkadajateApplication.class, args);
	}
	
	
	@Bean
	@Transactional
	CommandLineRunner initAdmin(UtilisateurRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
	    return args -> {
	        System.out.println("🚀 DÉMARRAGE INIT ADMIN");

	        long userCount = userRepository.count(); // ← ici ça bloque
	        System.out.println("🔍 Nombre d'utilisateurs trouvés : " + userCount);

	        if (userCount == 0) {
	            Role adminRole = roleRepository.findByNomrole("ADMIN").orElse(null);
	            if (adminRole == null) {
	                adminRole = new Role();
	                adminRole.setNomrole("ADMIN");
	                roleRepository.save(adminRole);
	                System.out.println("✅ Rôle ADMIN créé");
	            }

	            Utilisateur admin = new Utilisateur();
	            admin.setNom("TRAORE");
	            admin.setPrenom("Zoumana");
	            admin.setEmail("traorezoul7@gmail.com");
	            admin.setUsername("roosevelt9201");
	            admin.setTelephone("70636247");
	            admin.setPassword(passwordEncoder.encode("roosevelt9201"));
	            admin.setRole(adminRole);

	            userRepository.save(admin);
	            System.out.println("✅ Admin sauvegardé");
	        } else {
	            System.out.println("ℹ️ Utilisateurs déjà existants. Admin non créé.");
	        }
	    };
	}



}
