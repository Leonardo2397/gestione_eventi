package com.leonardoferrante.gestione_eventi.controller;


import com.leonardoferrante.gestione_eventi.entities.Evento;
import com.leonardoferrante.gestione_eventi.entities.Prenotazione;
import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.repositories.UtenteRepository;
import com.leonardoferrante.gestione_eventi.services.EventoService;
import com.leonardoferrante.gestione_eventi.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UtenteRepository utenteRepository;

    @PostMapping("/prenotazioni/{eventoId}")
    public ResponseEntity<?> prenotaEvento(@PathVariable Long eventoId, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Utente utente = utenteRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Optional<Evento> eventoPossibile = eventoService.getEventoById(eventoId);
        if (eventoPossibile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Evento evento = eventoPossibile.get();

        // verifica se prenotazione gia effettuata
        if (prenotazioneService.alreadyBooked(utente, evento)) {
            return ResponseEntity.badRequest().body("hai gia una prenotazione per questo evento");
        }

        //disponibilita posti
        long postiOccupati = prenotazioneService.countPrenotaziooniConfermate(evento);
        if (postiOccupati >= evento.getNumeroMassimoPartecipanti()) {
            return ResponseEntity.badRequest().body("l evento è al completo");
        }

        Prenotazione pren = Prenotazione.builder()
                .utente(utente)
                .evento(evento)
                .confermata(true)
                .build();

        Prenotazione salvata = prenotazioneService.creaPrenotazione(pren);
        return ResponseEntity.ok(salvata);
    }

    // GET /api/prenotazioni/mie -> lista prenotazioni dell'utente autenticato
    @GetMapping("/prenotazioni/mie")
    public ResponseEntity<List<Prenotazione>> miePrenotazioni(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Utente utente = utenteRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Prenotazione> list = prenotazioneService.getPrenotazioneById(utente.getId());
        list.forEach(p -> {
            if (p.getUtente() != null) p.getUtente().setPassword(null);
        });
    }


    // GET /api/eventi/{eventoId}/prenotazioni -> solo organizzatore dell'evento può vedere
    @GetMapping("/eventi/{eventoId}/prenotazioni")
    public ResponseEntity<?> prenotazioniPerEvento(@PathVariable Long eventoId, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Utente requester = utenteRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        Optional<Evento> eventoPossibile = eventoService.getEventoById(eventoId);
        if (eventoPossibile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Evento evento = eventoPossibile.get();

        // autorizzazione: solo l'organizzatore dell'evento può vedere le prenotazioni
        if (evento.getOrganizzatore() == null || !evento.getOrganizzatore().getEmail().equals(requester.getEmail())) {
            return ResponseEntity.status(403).body("non autorizzato");
        }
        

    }

}
