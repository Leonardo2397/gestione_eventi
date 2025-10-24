package com.leonardoferrante.gestione_eventi.services;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.entities.Prenotazione;
import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.repositories.PrenotazioneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    public long countPrenotaziooniConfermate(Evento evento) {
        return prenotazioneRepository.countByEvento(evento);
    }

    public boolean alreadyBooked(Utente utente, Evento evento) {
        return prenotazioneRepository.existsByUtenteAndEvento(utente, evento);
    }

    public Prenotazione creaPrenotazione(Prenotazione p) {
        return prenotazioneRepository.save(p);
    }

    public List<Prenotazione> getPrenotazioniPerEvento(Evento evento) {
        return prenotazioneRepository.findByEvento(evento);
    }

    public Optional<Prenotazione> getPrenotazioneById(Long id) {
        return prenotazioneRepository.findById(id);
    }

    @Transactional
    public void eliminaPrenotazione(Long id) {
        prenotazioneRepository.deleteById(id);
    }
}
