package com.leonardoferrante.gestione_eventi.repositories;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.entities.Prenotazione;
import com.leonardoferrante.gestione_eventi.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

long countByEvento(Evento evento);

boolean existsByUtenteAndEvento(Utente utente, Evento evento);

List<Prenotazione> findByUtente(Utente utente);

List<Prenotazione> findByEvento(Evento evento);

Optional<Prenotazione> findByIdAndUtente(Long id, Utente utente);
}
