package com.leonardoferrante.gestione_eventi.repositories;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface eventoRepository extends JpaRepository<Evento, Long> {
}
