package com.ikadjate.backend.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ikadjate.backend.config.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "role")

public class Role extends BaseEntity{
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String nomrole;
	@OneToMany(mappedBy = "role")
	@JsonIgnore
    private List<Utilisateur> utilisateurs;
}
