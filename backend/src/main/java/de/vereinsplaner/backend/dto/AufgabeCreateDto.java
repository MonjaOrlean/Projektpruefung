package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class AufgabeCreateDto {

    @NotBlank(message = "Titel darf nicht leer sein.")
    private String titel;

    private String beschreibung;

    private Long veranstaltungId;

    public AufgabeCreateDto() {
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

    public Long getVeranstaltungId() {
        return veranstaltungId;
    }

    public void setVeranstaltungId(Long veranstaltungId) {
        this.veranstaltungId = veranstaltungId;
    }
}
