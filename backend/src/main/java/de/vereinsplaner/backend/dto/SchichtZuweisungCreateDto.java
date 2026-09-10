package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.NotNull;

public class SchichtZuweisungCreateDto {

    @NotNull(message = "Schicht darf nicht leer sein.")
    private Long schichtId;

    @NotNull(message = "Mitglied darf nicht leer sein.")
    private Long mitgliedId;

    public SchichtZuweisungCreateDto() {
    }

    public Long getSchichtId() {
        return schichtId;
    }

    public void setSchichtId(Long schichtId) {
        this.schichtId = schichtId;
    }

    public Long getMitgliedId() {
        return mitgliedId;
    }

    public void setMitgliedId(Long mitgliedId) {
        this.mitgliedId = mitgliedId;
    }
}