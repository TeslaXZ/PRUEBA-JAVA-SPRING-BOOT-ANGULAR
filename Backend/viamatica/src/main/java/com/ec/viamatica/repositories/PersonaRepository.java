package com.ec.viamatica.repositories;

import com.ec.viamatica.entities.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
