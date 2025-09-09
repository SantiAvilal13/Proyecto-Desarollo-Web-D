package com.regata.repositories;

import com.regata.entities.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MapaRepository extends JpaRepository<Mapa, Long> {
    
    Optional<Mapa> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
}