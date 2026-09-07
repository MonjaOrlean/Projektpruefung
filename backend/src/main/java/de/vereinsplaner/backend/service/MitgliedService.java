package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Mitglied;
import de.vereinsplaner.backend.repository.MitgliedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MitgliedService {

    private final MitgliedRepository mitgliedRepository;

    public MitgliedService(MitgliedRepository mitgliedRepository) {
        this.mitgliedRepository = mitgliedRepository;
    }

    public List<Mitglied> alleMitgliederLaden() {
        return mitgliedRepository.findAll();
    }

    public Optional<Mitglied> mitgliedNachIdLaden(Long id) {
        return mitgliedRepository.findById(id);
    }

    public Mitglied mitgliedSpeichern(Mitglied mitglied) {
        return mitgliedRepository.save(mitglied);
    }

    public void mitgliedLoeschen(Long id) {
        mitgliedRepository.deleteById(id);
    }
}