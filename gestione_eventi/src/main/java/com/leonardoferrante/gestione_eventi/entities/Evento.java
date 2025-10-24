package com.leonardoferrante.gestione_eventi.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
 @Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String descrizione;
    private LocalDate dataEvento;
    private String luogo;
    private int numeroMassimoPartecipanti;

    @ManyToOne
    @JoinColumn(name = "organizzatore_id")
    private Utente organizzatore;
}
