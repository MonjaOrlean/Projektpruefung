package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "einsatzbereiche")
public class Einsatzbereich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String beschreibung;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "veranstaltung_id",
            nullable = false
    )
    private Veranstaltung veranstaltung;

    public Einsatzbereich() {
    }

    public Einsatzbereich(
            String name,
            String beschreibung,
            Veranstaltung veranstaltung
    ) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.veranstaltung = veranstaltung;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Veranstaltung getVeranstaltung() {
        return veranstaltung;
    }

    public void setVeranstaltung(
            Veranstaltung veranstaltung
    ) {
        this.veranstaltung = veranstaltung;
    }
}