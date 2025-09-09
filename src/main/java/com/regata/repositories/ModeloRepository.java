package com.regata.repositories;

import com.regata.entities.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    
    Optional<Modelo> findByNombre(String nombre);
    
    List<Modelo> findByColor(String color);
    
    List<Modelo> findByNombreContainingIgnoreCase(String nombre);
    
    boolean existsByNombre(String nombre);
    
}