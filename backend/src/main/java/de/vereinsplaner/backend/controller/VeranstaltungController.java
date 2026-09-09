package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.VeranstaltungCreateDto;
import de.vereinsplaner.backend.dto.VeranstaltungUpdateDto;
import de.vereinsplaner.backend.model.Veranstaltung;
import de.vereinsplaner.backend.service.VeranstaltungService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veranstaltungen")
@CrossOrigin(origins = "http://localhost:4200")
public class VeranstaltungController {

    private final VeranstaltungService veranstaltungService;

    public VeranstaltungController(VeranstaltungService veranstaltungService) {
        this.veranstaltungService = veranstaltungService;
    }

    @GetMapping
    public List<Veranstaltung> alleVeranstaltungenLaden() {
        return veranstaltungService.alleVeranstaltungenLaden();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veranstaltung> veranstaltungNachIdLaden(
            @PathVariable Long id) {

        return veranstaltungService.veranstaltungNachIdLaden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Veranstaltung veranstaltungAnlegen(
            @Valid @RequestBody VeranstaltungCreateDto dto) {

        Veranstaltung veranstaltung = new Veranstaltung();

        veranstaltung.setName(dto.getName());
        veranstaltung.setDatum(dto.getDatum());
        veranstaltung.setStartzeit(dto.getStartzeit());
        veranstaltung.setEndzeit(dto.getEndzeit());
        veranstaltung.setOrt(dto.getOrt());
        veranstaltung.setBeschreibung(dto.getBeschreibung());
        veranstaltung.setAktiv(true);

        return veranstaltungService.veranstaltungSpeichern(veranstaltung);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veranstaltung> veranstaltungBearbeiten(
            @PathVariable Long id,
            @Valid @RequestBody VeranstaltungUpdateDto dto) {

        return veranstaltungService.veranstaltungNachIdLaden(id)
                .map(vorhandeneVeranstaltung -> {

                    vorhandeneVeranstaltung.setName(dto.getName());
                    vorhandeneVeranstaltung.setDatum(dto.getDatum());
                    vorhandeneVeranstaltung.setStartzeit(dto.getStartzeit());
                    vorhandeneVeranstaltung.setEndzeit(dto.getEndzeit());
                    vorhandeneVeranstaltung.setOrt(dto.getOrt());
                    vorhandeneVeranstaltung.setBeschreibung(dto.getBeschreibung());
                    vorhandeneVeranstaltung.setAktiv(dto.isAktiv());

                    Veranstaltung gespeichert =
                            veranstaltungService.veranstaltungSpeichern(
                                    vorhandeneVeranstaltung
                            );

                    return ResponseEntity.ok(gespeichert);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> veranstaltungLoeschen(
            @PathVariable Long id) {

        if (veranstaltungService
                .veranstaltungNachIdLaden(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        veranstaltungService.veranstaltungLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}