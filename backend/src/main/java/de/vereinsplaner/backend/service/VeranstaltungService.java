package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Veranstaltung;
import de.vereinsplaner.backend.repository.VeranstaltungRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeranstaltungService {

    private final VeranstaltungRepository veranstaltungRepository;

    public VeranstaltungService(VeranstaltungRepository veranstaltungRepository) {
        this.veranstaltungRepository = veranstaltungRepository;
    }

    public List<Veranstaltung> alleVeranstaltungenLaden() {
        return veranstaltungRepository.findAll();
    }

    public Optional<Veranstaltung> veranstaltungNachIdLaden(Long id) {
        return veranstaltungRepository.findById(id);
    }

    public Veranstaltung veranstaltungSpeichern(Veranstaltung veranstaltung) {
        return veranstaltungRepository.save(veranstaltung);
    }

    public void veranstaltungLoeschen(Long id) {
        veranstaltungRepository.deleteById(id);
    }
}