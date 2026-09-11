package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.repository.SchichtRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchichtService {

    private final SchichtRepository schichtRepository;
    private final SchichtZuweisungService schichtZuweisungService;

    public SchichtService(
            SchichtRepository schichtRepository,
            SchichtZuweisungService schichtZuweisungService
    ) {
        this.schichtRepository = schichtRepository;
        this.schichtZuweisungService = schichtZuweisungService;
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

        schichtZuweisungService
                .zuweisungenNachSchichtLoeschen(id);

        schichtRepository.deleteById(id);
    }

}