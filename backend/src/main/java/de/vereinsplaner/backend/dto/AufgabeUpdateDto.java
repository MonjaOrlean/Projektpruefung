package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AufgabeUpdateDto {

    @NotBlank(message = "Titel darf nicht leer sein.")
    private String titel;

    private String beschreibung;

    private boolean erledigt;

    private Long veranstaltungId;

    public AufgabeUpdateDto() {
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

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }
}