package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aufgaben")
public class Aufgabe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titel;

    @Column(length = 1000)
    private String beschreibung;

    @Column(nullable = false)
    private boolean erledigt = false;

    @ManyToOne
    @JoinColumn(name = "veranstaltung_id")
    private Veranstaltung veranstaltung;

    public Aufgabe() {
    }

    public Aufgabe(
            String titel,
            String beschreibung,
            boolean erledigt,
            Veranstaltung veranstaltung
    ) {
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.erledigt = erledigt;
        this.veranstaltung = veranstaltung;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }

    public Veranstaltung getVeranstaltung() {
        return veranstaltung;
    }

    public void setVeranstaltung(Veranstaltung veranstaltung) {
        this.veranstaltung = veranstaltung;
    }
}
