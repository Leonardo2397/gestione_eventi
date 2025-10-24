package com.leonardoferrante.gestione_eventi.services;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public Evento creaEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public List<Evento> getAllEventi() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> getEventoById(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento aggiornaEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    public void eliminaEvento(Long id) {
        eventoRepository.deleteById(id);
    }
}