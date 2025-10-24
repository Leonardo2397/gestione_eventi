package com.leonardoferrante.gestione_eventi.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @ManyToOne
    @JoinColumn(name="utente_id")
    private Utente utente;


    @ManyToOne
    @JoinColumn(name="evento_id")
    private Evento evento;

    private boolean confermata;
}
