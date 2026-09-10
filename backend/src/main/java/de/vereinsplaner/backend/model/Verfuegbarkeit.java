package de.vereinsplaner.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "verfuegbarkeiten")
public class Verfuegbarkeit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datum;

    @Column(nullable = false)
    private LocalTime startzeit;

    @Column(nullable = false)
    private LocalTime endzeit;

    @Column(length = 500)
    private String bemerkung;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mitglied_id", nullable = false)
    private Mitglied mitglied;

    public Verfuegbarkeit() {
    }

    public Verfuegbarkeit(
            LocalDate datum,
            LocalTime startzeit,
            LocalTime endzeit,
            String bemerkung,
            Mitglied mitglied
    ) {
        this.datum = datum;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.bemerkung = bemerkung;
        this.mitglied = mitglied;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public Mitglied getMitglied() {
        return mitglied;
    }

    public void setMitglied(Mitglied mitglied) {
        this.mitglied = mitglied;
    }
}