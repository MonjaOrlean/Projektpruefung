package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Einsatzbereich;
import de.vereinsplaner.backend.repository.EinsatzbereichRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EinsatzbereichService {

    private final EinsatzbereichRepository einsatzbereichRepository;

    public EinsatzbereichService(
            EinsatzbereichRepository einsatzbereichRepository
    ) {
        this.einsatzbereichRepository = einsatzbereichRepository;
    }

    public List<Einsatzbereich> alleEinsatzbereicheLaden() {
        return einsatzbereichRepository.findAll();
    }

    public Optional<Einsatzbereich> einsatzbereichNachIdLaden(
            Long id
    ) {
        return einsatzbereichRepository.findById(id);
    }

    public List<Einsatzbereich> einsatzbereicheNachVeranstaltungLaden(
            Long veranstaltungId
    ) {
        return einsatzbereichRepository
                .findByVeranstaltungId(veranstaltungId);
    }

    public Einsatzbereich einsatzbereichSpeichern(
            Einsatzbereich einsatzbereich
    ) {
        return einsatzbereichRepository.save(einsatzbereich);
    }

    public void einsatzbereichLoeschen(
            Long id
    ) {
        einsatzbereichRepository.deleteById(id);
    }
}