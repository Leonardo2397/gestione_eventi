package com.leonardoferrante.gestione_eventi.services;


import com.leonardoferrante.gestione_eventi.entities.Role;
import com.leonardoferrante.gestione_eventi.entities.Utente;
import com.leonardoferrante.gestione_eventi.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //registrazione per utente normale
    public Utente registraUtente(Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRole(Role.ROLE_USER); //default
        return utenteRepository.save(utente);
    }

    //registra organizzatore
    public Utente registraOrganizzatore(Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utente.setRole(Role.ROLE_ORGANIZZATORE);
        return utenteRepository.save(utente);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        return new org.springframework.security.core.userdetails.User(
                utente.getEmail(),
                utente.getPassword(),
                List.of(new SimpleGrantedAuthority(utente.getRole().name()))
        );
    }
}
