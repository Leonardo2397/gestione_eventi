package com.leonardoferrante.gestione_eventi.controller;

import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

//    @Autowired
//    private UtenteRepository utenteRepository;
//
//    // tutti gli utenti
//    @GetMapping
//    public List<Utente> getAllUtenti() {
//        return utenteRepository.findAll();
//    }
//
//    //utente per id
//    @GetMapping("/{id}")
//    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
//        return utenteRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    //elimina utente
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
//        if(utenteRepository.existsById(id)) {
//            utenteRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}
@Autowired
private UtenteRepository utenteRepository;

    // Ottieni tutti gli utenti (solo ORGANIZZATORE)
    @GetMapping
    public ResponseEntity<List<Utente>> getAllUtenti(Authentication auth) {
        if (!isOrganizzatore(auth)) {
            return ResponseEntity.status(403).build();
        }

        List<Utente> utenti = utenteRepository.findAll().stream()
                .peek(u -> u.setPassword(null)) // nascondi password
                .collect(Collectors.toList());

        return ResponseEntity.ok(utenti);
    }

    // Ottieni utente per ID (solo ORGANIZZATORE o se è se stesso)
    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id, Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return utenteRepository.findById(id).map(utente -> {
            if (!utente.getEmail().equals(userDetails.getUsername()) && !isOrganizzatore(auth)) {
                return ResponseEntity.status(403).<Utente>build();
            }
            utente.setPassword(null);
            return ResponseEntity.ok(utente);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Elimina utente (solo ORGANIZZATORE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id, Authentication auth) {
        if (!isOrganizzatore(auth)) {
            return ResponseEntity.status(403).build();
        }

        if (!utenteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        utenteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper per controllare se l'utente loggato è ORGANIZZATORE
    private boolean isOrganizzatore(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZZATORE"));
    }
}
