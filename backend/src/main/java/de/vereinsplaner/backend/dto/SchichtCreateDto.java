package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class SchichtCreateDto {

    @NotBlank(message = "Name darf nicht leer sein.")
    private String name;

    @NotNull(message = "Datum darf nicht leer sein.")
    private LocalDate datum;

    @NotNull(message = "Startzeit darf nicht leer sein.")
    private LocalTime startzeit;

    @NotNull(message = "Endzeit darf nicht leer sein.")
    private LocalTime endzeit;

    @Min(value = 1, message = "Es muss mindestens eine Person benötigt werden.")
    private int benoetigtePersonen;

    private String beschreibung;

    private Long veranstaltungId;

    public SchichtCreateDto() {
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

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }

    @AssertTrue(message = "Die Endzeit darf nicht vor der Startzeit liegen.")
    public boolean isZeitspanneGueltig() {

        if (startzeit == null || endzeit == null) {
            return true;
        }

        return !endzeit.isBefore(startzeit);
    }
}