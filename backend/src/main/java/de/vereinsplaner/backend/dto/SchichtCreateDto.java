package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class SchichtCreateDto {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate datum;

    @NotNull
    private LocalTime startzeit;

    @NotNull
    private LocalTime endzeit;

    @Min(1)
    private int benoetigtePersonen;

    private String beschreibung;

    private Long veranstaltungId;

    private Long einsatzbereichId;

    public SchichtCreateDto() {
    }

    @AssertTrue(
            message = "Die Endzeit darf nicht vor der Startzeit liegen."
    )
    public boolean isZeitspanneGueltig() {

        if (
                startzeit == null ||
                        endzeit == null
        ) {
            return true;
        }

        return !endzeit.isBefore(startzeit);
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

    public void setBenoetigtePersonen(
            int benoetigtePersonen
    ) {
        this.benoetigtePersonen =
                benoetigtePersonen;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(
            String beschreibung
    ) {
        this.beschreibung =
                beschreibung;
    }

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(
            Long veranstaltungId
    ) {
        this.veranstaltungId =
                veranstaltungId;
    }

    public Long getEinsatzbereichId() {
        return einsatzbereichId;
    }

    public void setEinsatzbereichId(
            Long einsatzbereichId
    ) {
        this.einsatzbereichId =
                einsatzbereichId;
    }
}