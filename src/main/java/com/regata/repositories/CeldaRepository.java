package com.regata.repositories;

import com.regata.entities.Celda;
import com.regata.entities.Mapa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CeldaRepository extends JpaRepository<Celda, Long> {
    
    List<Celda> findByMapa(Mapa mapa);
    
    List<Celda> findByMapaId(Long mapaId);
    
    Optional<Celda> findByMapaAndCoordXAndCoordY(Mapa mapa, Integer coordX, Integer coordY);
    
    @Query("SELECT c FROM Celda c WHERE c.mapa.id = :mapaId AND c.coordX = :coordX AND c.coordY = :coordY")
    Optional<Celda> findByMapaIdAndCoordenadas(@Param("mapaId") Long mapaId, 
                                               @Param("coordX") Integer coordX, 
                                               @Param("coordY") Integer coordY);
    
    List<Celda> findByTipo(Celda.TipoCelda tipo);
    
    @Query("SELECT c FROM Celda c WHERE c.mapa.id = :mapaId ORDER BY c.coordY, c.coordX")
    List<Celda> findByMapaIdOrderedByCoordinates(@Param("mapaId") Long mapaId);
    
}