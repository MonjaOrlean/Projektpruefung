package de.vereinsplaner.backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class EinsatzplanSchichtDto {

    private Long schichtId;

    private String schichtName;

    private LocalDate datum;

    private LocalTime startzeit;

    private LocalTime endzeit;

    private String veranstaltungName;

    private String einsatzbereichName;

    private int benoetigtePersonen;

    private int zugewiesenePersonen;

    private int fehlendePersonen;

    private boolean unterbesetzt;

    private List<String> mitglieder;

    public EinsatzplanSchichtDto() {
    }

    public EinsatzplanSchichtDto(
            Long schichtId,
            String schichtName,
            LocalDate datum,
            LocalTime startzeit,
            LocalTime endzeit,
            String veranstaltungName,
            String einsatzbereichName,
            int benoetigtePersonen,
            int zugewiesenePersonen,
            int fehlendePersonen,
            boolean unterbesetzt,
            List<String> mitglieder
    ) {
        this.schichtId = schichtId;
        this.schichtName = schichtName;
        this.datum = datum;
        this.startzeit = startzeit;
        this.endzeit = endzeit;
        this.veranstaltungName = veranstaltungName;
        this.einsatzbereichName = einsatzbereichName;
        this.benoetigtePersonen = benoetigtePersonen;
        this.zugewiesenePersonen = zugewiesenePersonen;
        this.fehlendePersonen = fehlendePersonen;
        this.unterbesetzt = unterbesetzt;
        this.mitglieder = mitglieder;
    }

    public Long getSchichtId() {
        return schichtId;
    }

    public void setSchichtId(Long schichtId) {
        this.schichtId = schichtId;
    }

    public String getSchichtName() {
        return schichtName;
    }

    public void setSchichtName(String schichtName) {
        this.schichtName = schichtName;
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

    public String getVeranstaltungName() {
        return veranstaltungName;
    }

    public void setVeranstaltungName(String veranstaltungName) {
        this.veranstaltungName = veranstaltungName;
    }

    public String getEinsatzbereichName() {
        return einsatzbereichName;
    }

    public void setEinsatzbereichName(String einsatzbereichName) {
        this.einsatzbereichName = einsatzbereichName;
    }

    public int getBenoetigtePersonen() {
        return benoetigtePersonen;
    }

    public void setBenoetigtePersonen(int benoetigtePersonen) {
        this.benoetigtePersonen = benoetigtePersonen;
    }

    public int getZugewiesenePersonen() {
        return zugewiesenePersonen;
    }

    public void setZugewiesenePersonen(int zugewiesenePersonen) {
        this.zugewiesenePersonen = zugewiesenePersonen;
    }

    public int getFehlendePersonen() {
        return fehlendePersonen;
    }

    public void setFehlendePersonen(int fehlendePersonen) {
        this.fehlendePersonen = fehlendePersonen;
    }

    public boolean isUnterbesetzt() {
        return unterbesetzt;
    }

    public void setUnterbesetzt(boolean unterbesetzt) {
        this.unterbesetzt = unterbesetzt;
    }

    public List<String> getMitglieder() {
        return mitglieder;
    }

    public void setMitglieder(List<String> mitglieder) {
        this.mitglieder = mitglieder;
    }
}