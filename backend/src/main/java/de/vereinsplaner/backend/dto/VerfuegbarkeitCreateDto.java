package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class VerfuegbarkeitCreateDto {

    @NotNull(message = "Datum darf nicht leer sein.")
    private LocalDate datum;

    @NotNull(message = "Startzeit darf nicht leer sein.")
    private LocalTime startzeit;

    @NotNull(message = "Endzeit darf nicht leer sein.")
    private LocalTime endzeit;

    private String bemerkung;

    @NotNull(message = "Mitglied darf nicht leer sein.")
    private Long mitgliedId;

    public VerfuegbarkeitCreateDto() {
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

    public Long getMitgliedId() {
        return mitgliedId;
    }

    public void setMitgliedId(Long mitgliedId) {
        this.mitgliedId = mitgliedId;
    }

    @AssertTrue(message = "Die Endzeit darf nicht vor der Startzeit liegen.")
    public boolean isZeitspanneGueltig() {

        if (startzeit == null || endzeit == null) {
            return true;
        }

        return !endzeit.isBefore(startzeit);
    }
}