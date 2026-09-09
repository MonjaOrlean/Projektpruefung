package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.AufgabeCreateDto;
import de.vereinsplaner.backend.dto.AufgabeUpdateDto;
import de.vereinsplaner.backend.model.Aufgabe;
import de.vereinsplaner.backend.model.Veranstaltung;
import de.vereinsplaner.backend.service.AufgabeService;
import de.vereinsplaner.backend.service.VeranstaltungService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aufgaben")
@CrossOrigin(origins = "http://localhost:4200")
public class AufgabeController {

    private final AufgabeService aufgabeService;
    private final VeranstaltungService veranstaltungService;

    public AufgabeController(
            AufgabeService aufgabeService,
            VeranstaltungService veranstaltungService
    ) {
        this.aufgabeService = aufgabeService;
        this.veranstaltungService = veranstaltungService;
    }

    @GetMapping
    public List<Aufgabe> alleAufgabenLaden() {
        return aufgabeService.alleAufgabenLaden();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aufgabe> aufgabeNachIdLaden(
            @PathVariable Long id
    ) {
        return aufgabeService.aufgabeNachIdLaden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aufgabe> aufgabeAnlegen(
            @Valid @RequestBody AufgabeCreateDto dto
    ) {

        Veranstaltung veranstaltung = null;

        if (dto.getVeranstaltungId() != null) {

            veranstaltung = veranstaltungService
                    .veranstaltungNachIdLaden(dto.getVeranstaltungId())
                    .orElse(null);

            if (veranstaltung == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        Aufgabe aufgabe = new Aufgabe();

        aufgabe.setTitel(dto.getTitel());
        aufgabe.setBeschreibung(dto.getBeschreibung());
        aufgabe.setErledigt(false);
        aufgabe.setVeranstaltung(veranstaltung);

        Aufgabe gespeichert =
                aufgabeService.aufgabeSpeichern(aufgabe);

        return ResponseEntity.ok(gespeichert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aufgabe> aufgabeBearbeiten(
            @PathVariable Long id,
            @Valid @RequestBody AufgabeUpdateDto dto
    ) {

        return aufgabeService.aufgabeNachIdLaden(id)
                .map(vorhandeneAufgabe -> {

                    Veranstaltung veranstaltung = null;

                    if (dto.getVeranstaltungId() != null) {

                        veranstaltung = veranstaltungService
                                .veranstaltungNachIdLaden(
                                        dto.getVeranstaltungId()
                                )
                                .orElse(null);

                        if (veranstaltung == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .<Aufgabe>build();
                        }
                    }

                    vorhandeneAufgabe.setTitel(dto.getTitel());
                    vorhandeneAufgabe.setBeschreibung(
                            dto.getBeschreibung()
                    );
                    vorhandeneAufgabe.setErledigt(
                            dto.isErledigt()
                    );
                    vorhandeneAufgabe.setVeranstaltung(
                            veranstaltung
                    );

                    Aufgabe gespeichert =
                            aufgabeService.aufgabeSpeichern(
                                    vorhandeneAufgabe
                            );

                    return ResponseEntity.ok(gespeichert);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> aufgabeLoeschen(
            @PathVariable Long id
    ) {

        if (aufgabeService
                .aufgabeNachIdLaden(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        aufgabeService.aufgabeLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}