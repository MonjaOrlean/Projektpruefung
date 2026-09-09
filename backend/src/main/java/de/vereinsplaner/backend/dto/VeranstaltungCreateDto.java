package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.time.LocalTime;

public class VeranstaltungCreateDto {

    @NotBlank(message = "Name darf nicht leer sein.")
    private String name;

    @NotNull(message = "Datum darf nicht leer sein.")
    private LocalDate datum;

    private LocalTime startzeit;

    private LocalTime endzeit;

    private String ort;

    private String beschreibung;

    public VeranstaltungCreateDto() {
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

    @AssertTrue(message = "Die Endzeit darf nicht vor der Startzeit liegen.")
    public boolean isZeitspanneGueltig() {

        if (startzeit == null || endzeit == null) {
            return true;
        }

        return !endzeit.isBefore(startzeit);
    }
}