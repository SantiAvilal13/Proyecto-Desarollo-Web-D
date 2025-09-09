package com.regata.repositories;

import com.regata.entities.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {
    
    Optional<Jugador> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Jugador> findByNombreContainingIgnoreCase(String nombre);
    
}