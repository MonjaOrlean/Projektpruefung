package de.vereinsplaner.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MitgliedCreateDto {

    @NotBlank(message = "Vorname darf nicht leer sein.")
    private String vorname;

    @NotBlank(message = "Nachname darf nicht leer sein.")
    private String nachname;

    @Email(message = "Bitte eine gültige E-Mail-Adresse eingeben.")
    private String email;

    private String telefon;

    public MitgliedCreateDto() {
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}