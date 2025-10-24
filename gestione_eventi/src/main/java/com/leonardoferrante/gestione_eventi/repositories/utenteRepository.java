package com.leonardoferrante.gestione_eventi.repositories;


import com.leonardoferrante.gestione_eventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface utenteRepository extends JpaRepository <Utente, Long> {
}
