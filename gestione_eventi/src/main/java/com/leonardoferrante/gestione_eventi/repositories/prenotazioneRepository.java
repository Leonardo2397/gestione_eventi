package com.leonardoferrante.gestione_eventi.repositories;


import com.leonardoferrante.gestione_eventi.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface prenotazioneRepository extends JpaRepository<Prenotazione, Long> {
}
