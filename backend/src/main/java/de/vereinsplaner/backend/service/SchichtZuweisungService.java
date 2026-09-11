package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.model.SchichtZuweisung;
import de.vereinsplaner.backend.repository.SchichtZuweisungRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchichtZuweisungService {

    private final SchichtZuweisungRepository schichtZuweisungRepository;

    public SchichtZuweisungService(
            SchichtZuweisungRepository schichtZuweisungRepository
    ) {
        this.schichtZuweisungRepository = schichtZuweisungRepository;
    }

    public List<SchichtZuweisung> alleZuweisungenLaden() {
        return schichtZuweisungRepository.findAll();
    }

    public Optional<SchichtZuweisung> zuweisungNachIdLaden(
            Long id
    ) {
        return schichtZuweisungRepository.findById(id);
    }

    public List<SchichtZuweisung> zuweisungenNachSchichtLaden(
            Long schichtId
    ) {
        return schichtZuweisungRepository.findBySchichtId(schichtId);
    }

    public List<SchichtZuweisung> zuweisungenNachMitgliedLaden(
            Long mitgliedId
    ) {
        return schichtZuweisungRepository.findByMitgliedId(mitgliedId);
    }

    public boolean zuweisungExistiert(
            Long schichtId,
            Long mitgliedId
    ) {
        return schichtZuweisungRepository
                .existsBySchichtIdAndMitgliedId(
                        schichtId,
                        mitgliedId
                );
    }

    public boolean hatDoppelbelegung(
            Schicht neueSchicht,
            Long mitgliedId
    ) {

        List<SchichtZuweisung> vorhandeneZuweisungen =
                schichtZuweisungRepository
                        .findByMitgliedId(mitgliedId);

        for (SchichtZuweisung zuweisung : vorhandeneZuweisungen) {

            Schicht vorhandeneSchicht =
                    zuweisung.getSchicht();

            if (!vorhandeneSchicht
                    .getDatum()
                    .equals(neueSchicht.getDatum())) {

                continue;
            }

            boolean ueberschneidung =
                    neueSchicht.getStartzeit()
                            .isBefore(vorhandeneSchicht.getEndzeit())
                            &&
                            neueSchicht.getEndzeit()
                                    .isAfter(vorhandeneSchicht.getStartzeit());

            if (ueberschneidung) {
                return true;
            }
        }

        return false;
    }

    public int anzahlZugewiesenePersonen(
            Long schichtId
    ) {
        return schichtZuweisungRepository
                .findBySchichtId(schichtId)
                .size();
    }

    public boolean istUnterbesetzt(
            Schicht schicht
    ) {

        int zugewiesenePersonen =
                anzahlZugewiesenePersonen(
                        schicht.getId()
                );

        return zugewiesenePersonen
                < schicht.getBenoetigtePersonen();
    }

    public int fehlendePersonen(
            Schicht schicht
    ) {

        int zugewiesenePersonen =
                anzahlZugewiesenePersonen(
                        schicht.getId()
                );

        int fehlend =
                schicht.getBenoetigtePersonen()
                        - zugewiesenePersonen;

        return Math.max(fehlend, 0);
    }

    public SchichtZuweisung zuweisungSpeichern(
            SchichtZuweisung zuweisung
    ) {
        return schichtZuweisungRepository.save(zuweisung);
    }

    public void zuweisungLoeschen(Long id) {
        schichtZuweisungRepository.deleteById(id);
    }

    public void zuweisungenNachSchichtLoeschen(Long schichtId) {
        schichtZuweisungRepository.deleteBySchichtId(schichtId);
    }
    public void zuweisungenNachMitgliedLoeschen(Long mitgliedId) {
        schichtZuweisungRepository.deleteByMitgliedId(mitgliedId);
    }
}