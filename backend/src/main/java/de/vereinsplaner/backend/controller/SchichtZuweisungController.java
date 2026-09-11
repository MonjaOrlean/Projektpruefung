package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.SchichtZuweisungCreateDto;
import de.vereinsplaner.backend.model.Mitglied;
import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.model.SchichtZuweisung;
import de.vereinsplaner.backend.service.MitgliedService;
import de.vereinsplaner.backend.service.SchichtService;
import de.vereinsplaner.backend.service.SchichtZuweisungService;
import de.vereinsplaner.backend.service.VerfuegbarkeitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schicht-zuweisungen")
@CrossOrigin(origins = "http://localhost:4200")
public class SchichtZuweisungController {

    private final SchichtZuweisungService schichtZuweisungService;
    private final SchichtService schichtService;
    private final MitgliedService mitgliedService;
    private final VerfuegbarkeitService verfuegbarkeitService;

    public SchichtZuweisungController(
            SchichtZuweisungService schichtZuweisungService,
            SchichtService schichtService,
            MitgliedService mitgliedService,
            VerfuegbarkeitService verfuegbarkeitService
    ) {
        this.schichtZuweisungService = schichtZuweisungService;
        this.schichtService = schichtService;
        this.mitgliedService = mitgliedService;
        this.verfuegbarkeitService = verfuegbarkeitService;
    }

    @GetMapping
    public List<SchichtZuweisung> alleZuweisungenLaden() {
        return schichtZuweisungService.alleZuweisungenLaden();
    }

    @GetMapping("/schicht/{schichtId}")
    public List<SchichtZuweisung> zuweisungenNachSchichtLaden(
            @PathVariable Long schichtId
    ) {
        return schichtZuweisungService
                .zuweisungenNachSchichtLaden(schichtId);
    }

    @GetMapping("/mitglied/{mitgliedId}")
    public List<SchichtZuweisung> zuweisungenNachMitgliedLaden(
            @PathVariable Long mitgliedId
    ) {
        return schichtZuweisungService
                .zuweisungenNachMitgliedLaden(mitgliedId);
    }

    @GetMapping("/schicht/{schichtId}/status")
    public ResponseEntity<?> besetzungsstatusLaden(
            @PathVariable Long schichtId
    ) {

        Schicht schicht = schichtService
                .schichtNachIdLaden(schichtId)
                .orElse(null);

        if (schicht == null) {
            return ResponseEntity.notFound().build();
        }

        int zugewiesen =
                schichtZuweisungService
                        .anzahlZugewiesenePersonen(schichtId);

        int benoetigt =
                schicht.getBenoetigtePersonen();

        int fehlend =
                schichtZuweisungService
                        .fehlendePersonen(schicht);

        boolean unterbesetzt =
                schichtZuweisungService
                        .istUnterbesetzt(schicht);

        return ResponseEntity.ok(
                new java.util.HashMap<String, Object>() {{
                    put("schichtId", schichtId);
                    put("benoetigtePersonen", benoetigt);
                    put("zugewiesenePersonen", zugewiesen);
                    put("fehlendePersonen", fehlend);
                    put("unterbesetzt", unterbesetzt);
                }}
        );
    }

    @GetMapping("/schicht/{schichtId}/verfuegbare-mitglieder")
    public ResponseEntity<?> verfuegbareMitgliederFuerSchichtLaden(
            @PathVariable Long schichtId
    ) {

        Schicht schicht = schichtService
                .schichtNachIdLaden(schichtId)
                .orElse(null);

        if (schicht == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                verfuegbarkeitService
                        .verfuegbareMitgliederFuerSchicht(schicht)
        );
    }

    @PostMapping
    public ResponseEntity<?> zuweisungAnlegen(
            @Valid @RequestBody SchichtZuweisungCreateDto dto
    ) {

        Schicht schicht = schichtService
                .schichtNachIdLaden(dto.getSchichtId())
                .orElse(null);

        if (schicht == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Die ausgewählte Schicht existiert nicht.");
        }

        Mitglied mitglied = mitgliedService
                .mitgliedNachIdLaden(dto.getMitgliedId())
                .orElse(null);

        if (mitglied == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Das ausgewählte Mitglied existiert nicht.");
        }

        if (schichtZuweisungService.zuweisungExistiert(
                schicht.getId(),
                mitglied.getId()
        )) {
            return ResponseEntity
                    .badRequest()
                    .body("Das Mitglied ist dieser Schicht bereits zugewiesen.");
        }

        if (!verfuegbarkeitService.istMitgliedFuerSchichtVerfuegbar(
                mitglied.getId(),
                schicht
        )) {
            return ResponseEntity
                    .badRequest()
                    .body("Das Mitglied ist für diese Schicht nicht vollständig verfügbar.");
        }

        if (schichtZuweisungService.hatDoppelbelegung(
                schicht,
                mitglied.getId()
        )) {
            return ResponseEntity
                    .badRequest()
                    .body("Das Mitglied ist in diesem Zeitraum bereits einer anderen Schicht zugewiesen.");
        }

        SchichtZuweisung zuweisung =
                new SchichtZuweisung();

        zuweisung.setSchicht(schicht);
        zuweisung.setMitglied(mitglied);

        SchichtZuweisung gespeichert =
                schichtZuweisungService
                        .zuweisungSpeichern(zuweisung);

        return ResponseEntity.ok(gespeichert);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> zuweisungLoeschen(
            @PathVariable Long id
    ) {

        if (schichtZuweisungService
                .zuweisungNachIdLaden(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        schichtZuweisungService.zuweisungLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}