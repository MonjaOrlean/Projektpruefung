package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.model.Aufgabe;
import de.vereinsplaner.backend.repository.AufgabeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AufgabeService {

    private final AufgabeRepository aufgabeRepository;

    public AufgabeService(AufgabeRepository aufgabeRepository) {
        this.aufgabeRepository = aufgabeRepository;
    }

    public List<Aufgabe> alleAufgabenLaden() {
        return aufgabeRepository.findAll();
    }

    public Optional<Aufgabe> aufgabeNachIdLaden(Long id) {
        return aufgabeRepository.findById(id);
    }

    public Aufgabe aufgabeSpeichern(Aufgabe aufgabe) {
        return aufgabeRepository.save(aufgabe);
    }

    public void aufgabeLoeschen(Long id) {
        aufgabeRepository.deleteById(id);
    }
}
