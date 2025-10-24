package com.leonardoferrante.gestione_eventi.controller;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.repositories.EventoRepository;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;



    @GetMapping
    public List<Evento> gettAllEventi() {
        return eventoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        return evento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Evento> creaEvento(@RequestBody Evento nuovoEvento) {
        Evento salvato = eventoRepository.save(nuovoEvento);
        return ResponseEntity.ok(salvato);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Evento> aggiornaEvento(@PathVariable Long id, @RequestBody Evento eventoAggiornato) {
        return eventoRepository.findById(id).map(evento -> {
            evento.setTitolo(eventoAggiornato.getTitolo());
            evento.setDescrizione(eventoAggiornato.getDescrizione());
            evento.setDataEvento(eventoAggiornato.getDataEvento());
            evento.setLuogo(eventoAggiornato.getLuogo());
            evento.setNumeroMassimoPartecipanti(eventoAggiornato.getNumeroMassimoPartecipanti());
            Evento aggiornato= eventoRepository.save(evento);
            return ResponseEntity.ok(aggiornato);
        })
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) {
        if(eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
