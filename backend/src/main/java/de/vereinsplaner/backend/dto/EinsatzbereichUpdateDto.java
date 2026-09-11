package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EinsatzbereichUpdateDto {

    @NotBlank
    private String name;

    private String beschreibung;

    @NotNull
    private Long veranstaltungId;

    public EinsatzbereichUpdateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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