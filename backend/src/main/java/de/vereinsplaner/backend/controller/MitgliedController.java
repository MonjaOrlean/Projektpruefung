package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.MitgliedCreateDto;
import de.vereinsplaner.backend.dto.MitgliedUpdateDto;
import de.vereinsplaner.backend.model.Mitglied;
import de.vereinsplaner.backend.service.MitgliedService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mitglieder")
@CrossOrigin(origins = "http://localhost:4200")
public class MitgliedController {

    private final MitgliedService mitgliedService;

    public MitgliedController(MitgliedService mitgliedService) {
        this.mitgliedService = mitgliedService;
    }

    @GetMapping
    public List<Mitglied> alleMitgliederLaden() {
        return mitgliedService.alleMitgliederLaden();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mitglied> mitgliedNachIdLaden(@PathVariable Long id) {
        return mitgliedService.mitgliedNachIdLaden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mitglied mitgliedAnlegen(@Valid @RequestBody MitgliedCreateDto dto) {

        Mitglied mitglied = new Mitglied();

        mitglied.setVorname(dto.getVorname());
        mitglied.setNachname(dto.getNachname());
        mitglied.setEmail(dto.getEmail());
        mitglied.setTelefon(dto.getTelefon());
        mitglied.setAktiv(true);

        return mitgliedService.mitgliedSpeichern(mitglied);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mitglied> mitgliedBearbeiten(
            @PathVariable Long id,
            @Valid @RequestBody MitgliedUpdateDto dto) {

        return mitgliedService.mitgliedNachIdLaden(id)
                .map(vorhandenesMitglied -> {

                    vorhandenesMitglied.setVorname(dto.getVorname());
                    vorhandenesMitglied.setNachname(dto.getNachname());
                    vorhandenesMitglied.setEmail(dto.getEmail());
                    vorhandenesMitglied.setTelefon(dto.getTelefon());
                    vorhandenesMitglied.setAktiv(dto.isAktiv());

                    Mitglied gespeichert =
                            mitgliedService.mitgliedSpeichern(vorhandenesMitglied);

                    return ResponseEntity.ok(gespeichert);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> mitgliedLoeschen(@PathVariable Long id) {

        if (mitgliedService.mitgliedNachIdLaden(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mitgliedService.mitgliedLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}