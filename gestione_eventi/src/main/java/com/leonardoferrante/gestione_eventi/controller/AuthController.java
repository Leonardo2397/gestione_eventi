package com.leonardoferrante.gestione_eventi.controller;


import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtenteService utenteService;

    //registra utente
    @PostMapping("/register")
    public ResponseEntity<Utente> registra(@RequestBody Utente utente) {
        Utente registrato= utenteService.registraUtente(utente);
        registrato.setPassword(null);//non torna la pw cosi
        return ResponseEntity.ok(registrato);
    }

    //registra organizzatore
    @PostMapping("/register-organizzatore")
    public ResponseEntity<Utente> registraOrganizzatore(@RequestBody Utente utente) {
        Utente registrato = utenteService.registraOrganizzatore(utente);
        registrato.setPassword(null);
        return ResponseEntity.ok(registrato);
    }
}
