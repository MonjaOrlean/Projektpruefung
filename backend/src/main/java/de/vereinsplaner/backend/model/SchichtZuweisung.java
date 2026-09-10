package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "schicht_zuweisungen",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"schicht_id", "mitglied_id"}
                )
        }
)
public class SchichtZuweisung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "schicht_id", nullable = false)
    private Schicht schicht;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mitglied_id", nullable = false)
    private Mitglied mitglied;

    public SchichtZuweisung() {
    }

    public SchichtZuweisung(
            Schicht schicht,
            Mitglied mitglied
    ) {
        this.schicht = schicht;
        this.mitglied = mitglied;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Schicht getSchicht() {
        return schicht;
    }

    public void setSchicht(Schicht schicht) {
        this.schicht = schicht;
    }

    public Mitglied getMitglied() {
        return mitglied;
    }

    public void setMitglied(Mitglied mitglied) {
        this.mitglied = mitglied;
    }
}