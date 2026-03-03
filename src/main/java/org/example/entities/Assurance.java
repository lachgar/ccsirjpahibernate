package org.example.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assurances")
public class Assurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private TypeAssurance type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false, length = 255)
    private String couverture;

    @OneToMany(mappedBy = "assurance", fetch = FetchType.LAZY)
    private List<Contrat> contrats = new ArrayList<>();

    public Assurance() {}

    public Assurance(TypeAssurance type, BigDecimal montant, String couverture) {
        this.type = type;
        this.montant = montant;
        this.couverture = couverture;
    }

    // Getters/Setters
    public Long getId() { return id; }

    public TypeAssurance getType() { return type; }
    public void setType(TypeAssurance type) { this.type = type; }

    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }

    public String getCouverture() { return couverture; }
    public void setCouverture(String couverture) { this.couverture = couverture; }

    public List<Contrat> getContrats() { return contrats; }
    public void setContrats(List<Contrat> contrats) { this.contrats = contrats; }
}