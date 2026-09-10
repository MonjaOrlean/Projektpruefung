package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.model.Verfuegbarkeit;
import de.vereinsplaner.backend.repository.VerfuegbarkeitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VerfuegbarkeitService {

    private final VerfuegbarkeitRepository verfuegbarkeitRepository;

    public VerfuegbarkeitService(
            VerfuegbarkeitRepository verfuegbarkeitRepository
    ) {
        this.verfuegbarkeitRepository = verfuegbarkeitRepository;
    }

    public List<Verfuegbarkeit> alleVerfuegbarkeitenLaden() {
        return verfuegbarkeitRepository.findAll();
    }

    public Optional<Verfuegbarkeit> verfuegbarkeitNachIdLaden(Long id) {
        return verfuegbarkeitRepository.findById(id);
    }

    public List<Verfuegbarkeit> verfuegbarkeitenNachMitgliedLaden(
            Long mitgliedId
    ) {
        return verfuegbarkeitRepository
                .findByMitgliedId(mitgliedId);
    }

    public boolean istMitgliedFuerSchichtVerfuegbar(
            Long mitgliedId,
            Schicht schicht
    ) {

        List<Verfuegbarkeit> verfuegbarkeiten =
                verfuegbarkeitRepository
                        .findByMitgliedIdAndDatum(
                                mitgliedId,
                                schicht.getDatum()
                        );

        for (Verfuegbarkeit verfuegbarkeit : verfuegbarkeiten) {

            boolean startPasst =
                    !schicht.getStartzeit()
                            .isBefore(verfuegbarkeit.getStartzeit());

            boolean endePasst =
                    !schicht.getEndzeit()
                            .isAfter(verfuegbarkeit.getEndzeit());

            if (startPasst && endePasst) {
                return true;
            }
        }

        return false;
    }

    public Verfuegbarkeit verfuegbarkeitSpeichern(
            Verfuegbarkeit verfuegbarkeit
    ) {
        return verfuegbarkeitRepository.save(verfuegbarkeit);
    }

    public void verfuegbarkeitLoeschen(Long id) {
        verfuegbarkeitRepository.deleteById(id);
    }
}