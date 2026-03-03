package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "contrats")
public class Contrat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "assurance_id", nullable = false)
    private Assurance assurance;

    public Contrat() {}

    public Contrat(LocalDate dateDebut, LocalDate dateFin, Client client, Assurance assurance) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.client = client;
        this.assurance = assurance;
    }

    // Optionnel (utile pour “contrats actifs”)
    @Transient
    public boolean isActif(LocalDate reference) {
        return dateFin != null && dateFin.isAfter(reference);
    }

    // Getters/Setters
    public Long getId() { return id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Assurance getAssurance() { return assurance; }
    public void setAssurance(Assurance assurance) { this.assurance = assurance; }
}