package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schichten")
public class Schicht {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private LocalTime startzeit;

    @Column(nullable = false)
    private LocalTime endzeit;

    @Column(nullable = false)
    private int benoetigtePersonen;

    @Column(length = 1000)
    private String beschreibung;

    @ManyToOne
    @JoinColumn(name = "veranstaltung_id")
    private Veranstaltung veranstaltung;

    public Schicht() {
    }

    public Schicht(
            String name,
            LocalDate datum,
            LocalTime startzeit,
            LocalTime endzeit,
            int benoetigtePersonen,
            String beschreibung,
            Veranstaltung veranstaltung
    ) {
        this.name = name;
        this.datum = datum;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.benoetigtePersonen = benoetigtePersonen;
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

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public LocalTime getStartzeit() {
        return startzeit;
    }

    public void setStartzeit(LocalTime startzeit) {
        this.startzeit = startzeit;
    }

    public LocalTime getEndzeit() {
        return endzeit;
    }

    public void setEndzeit(LocalTime endzeit) {
        this.endzeit = endzeit;
    }

    public int getBenoetigtePersonen() {
        return benoetigtePersonen;
    }

    public void setBenoetigtePersonen(int benoetigtePersonen) {
        this.benoetigtePersonen = benoetigtePersonen;
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

    public void setVeranstaltung(Veranstaltung veranstaltung) {
        this.veranstaltung = veranstaltung;
    }
}