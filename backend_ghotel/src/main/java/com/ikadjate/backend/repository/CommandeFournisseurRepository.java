// CommandeFournisseurRepository.java
package com.ikadjate.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ikadjate.backend.model.CommandeFournisseur;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Long> {
}
