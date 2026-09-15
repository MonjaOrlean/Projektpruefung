package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.EinsatzbereichCreateDto;
import de.vereinsplaner.backend.dto.EinsatzbereichUpdateDto;
import de.vereinsplaner.backend.model.Einsatzbereich;
import de.vereinsplaner.backend.model.Veranstaltung;
import de.vereinsplaner.backend.service.EinsatzbereichService;
import de.vereinsplaner.backend.service.VeranstaltungService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/einsatzbereiche")
@CrossOrigin(origins = {"http://localhost:4200", "http://tauri.localhost"})
public class EinsatzbereichController {

    private final EinsatzbereichService einsatzbereichService;
    private final VeranstaltungService veranstaltungService;

    public EinsatzbereichController(
            EinsatzbereichService einsatzbereichService,
            VeranstaltungService veranstaltungService
    ) {
        this.einsatzbereichService = einsatzbereichService;
        this.veranstaltungService = veranstaltungService;
    }

    @GetMapping
    public ResponseEntity<List<Einsatzbereich>> alleEinsatzbereicheLaden() {

        List<Einsatzbereich> einsatzbereiche =
                einsatzbereichService.alleEinsatzbereicheLaden();

        return ResponseEntity.ok(einsatzbereiche);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Einsatzbereich> einsatzbereichNachIdLaden(
            @PathVariable Long id
    ) {

        Optional<Einsatzbereich> einsatzbereich =
                einsatzbereichService.einsatzbereichNachIdLaden(id);

        return einsatzbereich
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @GetMapping("/veranstaltung/{veranstaltungId}")
    public ResponseEntity<List<Einsatzbereich>>
    einsatzbereicheNachVeranstaltungLaden(
            @PathVariable Long veranstaltungId
    ) {

        List<Einsatzbereich> einsatzbereiche =
                einsatzbereichService
                        .einsatzbereicheNachVeranstaltungLaden(
                                veranstaltungId
                        );

        return ResponseEntity.ok(einsatzbereiche);
    }

    @PostMapping
    public ResponseEntity<?> einsatzbereichAnlegen(
            @Valid
            @RequestBody
            EinsatzbereichCreateDto dto
    ) {

        Optional<Veranstaltung> veranstaltungOptional =
                veranstaltungService
                        .veranstaltungNachIdLaden(
                                dto.getVeranstaltungId()
                        );

        if (veranstaltungOptional.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Die angegebene Veranstaltung existiert nicht."
                    );
        }

        Einsatzbereich einsatzbereich =
                new Einsatzbereich();

        einsatzbereich.setName(
                dto.getName()
        );

        einsatzbereich.setBeschreibung(
                dto.getBeschreibung()
        );

        einsatzbereich.setVeranstaltung(
                veranstaltungOptional.get()
        );

        Einsatzbereich gespeichert =
                einsatzbereichService
                        .einsatzbereichSpeichern(
                                einsatzbereich
                        );

        return ResponseEntity.ok(gespeichert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> einsatzbereichBearbeiten(
            @PathVariable Long id,
            @Valid
            @RequestBody
            EinsatzbereichUpdateDto dto
    ) {

        Optional<Einsatzbereich> einsatzbereichOptional =
                einsatzbereichService
                        .einsatzbereichNachIdLaden(id);

        if (einsatzbereichOptional.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        Optional<Veranstaltung> veranstaltungOptional =
                veranstaltungService
                        .veranstaltungNachIdLaden(
                                dto.getVeranstaltungId()
                        );

        if (veranstaltungOptional.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Die angegebene Veranstaltung existiert nicht."
                    );
        }

        Einsatzbereich einsatzbereich =
                einsatzbereichOptional.get();

        einsatzbereich.setName(
                dto.getName()
        );

        einsatzbereich.setBeschreibung(
                dto.getBeschreibung()
        );

        einsatzbereich.setVeranstaltung(
                veranstaltungOptional.get()
        );

        Einsatzbereich gespeichert =
                einsatzbereichService
                        .einsatzbereichSpeichern(
                                einsatzbereich
                        );

        return ResponseEntity.ok(gespeichert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> einsatzbereichLoeschen(
            @PathVariable Long id
    ) {

        Optional<Einsatzbereich> einsatzbereich =
                einsatzbereichService
                        .einsatzbereichNachIdLaden(id);

        if (einsatzbereich.isEmpty()) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        einsatzbereichService
                .einsatzbereichLoeschen(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
