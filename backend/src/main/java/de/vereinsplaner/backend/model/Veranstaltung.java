package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "veranstaltungen")
public class Veranstaltung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate datum;

    private LocalTime startzeit;

    private LocalTime endzeit;

    private String ort;

    @Column(length = 1000)
    private String beschreibung;

    @Column(nullable = false)
    private boolean aktiv = true;

    public Veranstaltung() {
    }

    public Veranstaltung(
            String name,
            LocalDate datum,
            LocalTime startzeit,
            LocalTime endzeit,
            String ort,
            String beschreibung,
            boolean aktiv
    ) {
        this.name = name;
        this.datum = datum;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.ort = ort;
        this.beschreibung = beschreibung;
        this.aktiv = aktiv;
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

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }
}