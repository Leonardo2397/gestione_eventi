package com.leonardoferrante.gestione_eventi.controller;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.repositories.EventoRepository;
import com.leonardoferrante.gestione_eventi.repositories.UtenteRepository;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/eventi")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UtenteRepository utenteRepository;


    //tutti gli eventi
    @GetMapping
    public List<Evento> gettAllEventi() {
        return eventoRepository.findAll();
    }


    //evento tramite id
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //crea evento (solo organizzatore)
    @PostMapping
    public ResponseEntity<Evento> creaEvento(@RequestBody Evento nuovoEvento) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Utente organizzatore = utenteRepository.findByEmail(userDetails.getUsername())
                .orElseThrow();
        nuovoEvento.setOrganizzatore(organizzatore);
        Evento salvato = eventoRepository.save(nuovoEvento);
        return ResponseEntity.ok(salvato);
    }

    //aggiorna evento (solo organizzatore)
    @PutMapping("/{id}")
    public ResponseEntity<Evento> aggiornaEvento(@PathVariable Long id, @RequestBody Evento eventoAggiornato) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return eventoRepository.findById(id).map(evento -> {
            if (!evento.getOrganizzatore().getEmail().equals(userDetails.getUsername())) {
                return ResponseEntity.status(403).<Evento>build();
            }
            evento.setTitolo(eventoAggiornato.getTitolo());
            evento.setDescrizione(eventoAggiornato.getDescrizione());
            evento.setDataEvento(eventoAggiornato.getDataEvento());
            evento.setLuogo(eventoAggiornato.getLuogo());
            evento.setNumeroMassimoPartecipanti(eventoAggiornato.getNumeroMassimoPartecipanti());
            Evento aggiornato = eventoRepository.save(evento);
            return ResponseEntity.ok(aggiornato);
        }).orElseGet(() -> ResponseEntity.notFound().build());

    }


    // elimina (organizzatore)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//
//        return eventoRepository.findById(id).map(evento -> {
//            if (!evento.getOrganizzatore().getEmail().equals(userDetails.getUsername())) {
//                return ResponseEntity.<Void>status(403).body(null);
//            }
//            eventoRepository.delete(evento);
//            return ResponseEntity.noContent().build();
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//
//        }
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaEvento(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        Optional<Evento> optionalEvento = eventoRepository.findById(id);
        if (optionalEvento.isEmpty()) {
            return ResponseEntity.notFound().<Void>build();
        }

        Evento evento = optionalEvento.get();
        if (!evento.getOrganizzatore().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).<Void>build();
        }

        eventoRepository.delete(evento);
        return ResponseEntity.noContent().build();
    }
}
