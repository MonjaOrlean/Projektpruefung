package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.SchichtCreateDto;
import de.vereinsplaner.backend.dto.SchichtUpdateDto;
import de.vereinsplaner.backend.model.Einsatzbereich;
import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.model.Veranstaltung;
import de.vereinsplaner.backend.service.EinsatzbereichService;
import de.vereinsplaner.backend.service.SchichtService;
import de.vereinsplaner.backend.service.VeranstaltungService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schichten")
@CrossOrigin(origins = {"http://localhost:4200", "http://tauri.localhost"})
public class SchichtController {

    private final SchichtService schichtService;
    private final VeranstaltungService veranstaltungService;
    private final EinsatzbereichService einsatzbereichService;

    public SchichtController(
            SchichtService schichtService,
            VeranstaltungService veranstaltungService,
            EinsatzbereichService einsatzbereichService
    ) {
        this.schichtService = schichtService;
        this.veranstaltungService = veranstaltungService;
        this.einsatzbereichService = einsatzbereichService;
    }

    @GetMapping
    public List<Schicht> alleSchichtenLaden() {
        return schichtService.alleSchichtenLaden();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schicht> schichtNachIdLaden(
            @PathVariable Long id
    ) {
        return schichtService.schichtNachIdLaden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Schicht> schichtAnlegen(
            @Valid @RequestBody SchichtCreateDto dto
    ) {

        Veranstaltung veranstaltung = null;
        Einsatzbereich einsatzbereich = null;

        if (dto.getVeranstaltungId() != null) {

            veranstaltung = veranstaltungService
                    .veranstaltungNachIdLaden(dto.getVeranstaltungId())
                    .orElse(null);

            if (veranstaltung == null) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (dto.getEinsatzbereichId() != null) {

            einsatzbereich = einsatzbereichService
                    .einsatzbereichNachIdLaden(dto.getEinsatzbereichId())
                    .orElse(null);

            if (einsatzbereich == null) {
                return ResponseEntity.badRequest().build();
            }

            if (veranstaltung == null) {
                return ResponseEntity.badRequest().build();
            }

            if (!einsatzbereich.getVeranstaltung().getId()
                    .equals(veranstaltung.getId())) {

                return ResponseEntity.badRequest().build();
            }
        }

        Schicht schicht = new Schicht();

        schicht.setName(dto.getName());
        schicht.setDatum(dto.getDatum());
        schicht.setStartzeit(dto.getStartzeit());
        schicht.setEndzeit(dto.getEndzeit());
        schicht.setBenoetigtePersonen(dto.getBenoetigtePersonen());
        schicht.setBeschreibung(dto.getBeschreibung());
        schicht.setVeranstaltung(veranstaltung);
        schicht.setEinsatzbereich(einsatzbereich);

        Schicht gespeichert =
                schichtService.schichtSpeichern(schicht);

        return ResponseEntity.ok(gespeichert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schicht> schichtBearbeiten(
            @PathVariable Long id,
            @Valid @RequestBody SchichtUpdateDto dto
    ) {

        return schichtService.schichtNachIdLaden(id)
                .map(vorhandeneSchicht -> {

                    Veranstaltung veranstaltung = null;
                    Einsatzbereich einsatzbereich = null;

                    if (dto.getVeranstaltungId() != null) {

                        veranstaltung = veranstaltungService
                                .veranstaltungNachIdLaden(
                                        dto.getVeranstaltungId()
                                )
                                .orElse(null);

                        if (veranstaltung == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .<Schicht>build();
                        }
                    }

                    if (dto.getEinsatzbereichId() != null) {

                        einsatzbereich = einsatzbereichService
                                .einsatzbereichNachIdLaden(
                                        dto.getEinsatzbereichId()
                                )
                                .orElse(null);

                        if (einsatzbereich == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .<Schicht>build();
                        }

                        if (veranstaltung == null) {
                            return ResponseEntity
                                    .badRequest()
                                    .<Schicht>build();
                        }

                        if (!einsatzbereich.getVeranstaltung().getId()
                                .equals(veranstaltung.getId())) {

                            return ResponseEntity
                                    .badRequest()
                                    .<Schicht>build();
                        }
                    }

                    vorhandeneSchicht.setName(dto.getName());
                    vorhandeneSchicht.setDatum(dto.getDatum());
                    vorhandeneSchicht.setStartzeit(dto.getStartzeit());
                    vorhandeneSchicht.setEndzeit(dto.getEndzeit());
                    vorhandeneSchicht.setBenoetigtePersonen(
                            dto.getBenoetigtePersonen()
                    );
                    vorhandeneSchicht.setBeschreibung(
                            dto.getBeschreibung()
                    );
                    vorhandeneSchicht.setVeranstaltung(
                            veranstaltung
                    );
                    vorhandeneSchicht.setEinsatzbereich(
                            einsatzbereich
                    );

                    Schicht gespeichert =
                            schichtService.schichtSpeichern(
                                    vorhandeneSchicht
                            );

                    return ResponseEntity.ok(gespeichert);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> schichtLoeschen(
            @PathVariable Long id
    ) {

        if (schichtService
                .schichtNachIdLaden(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        schichtService.schichtLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}
