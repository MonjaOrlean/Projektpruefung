package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.repository.SchichtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchichtService {

    private final SchichtRepository schichtRepository;

    public SchichtService(SchichtRepository schichtRepository) {
        this.schichtRepository = schichtRepository;
    }

    public List<Schicht> alleSchichtenLaden() {
        return schichtRepository.findAll();
    }

    public Optional<Schicht> schichtNachIdLaden(Long id) {
        return schichtRepository.findById(id);
    }

    public Schicht schichtSpeichern(Schicht schicht) {
        return schichtRepository.save(schicht);
    }

    public void schichtLoeschen(Long id) {
        schichtRepository.deleteById(id);
    }
}