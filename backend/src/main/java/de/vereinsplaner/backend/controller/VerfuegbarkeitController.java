package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.VerfuegbarkeitCreateDto;
import de.vereinsplaner.backend.dto.VerfuegbarkeitUpdateDto;
import de.vereinsplaner.backend.model.Mitglied;
import de.vereinsplaner.backend.model.Verfuegbarkeit;
import de.vereinsplaner.backend.service.MitgliedService;
import de.vereinsplaner.backend.service.VerfuegbarkeitService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/verfuegbarkeiten")
@CrossOrigin(origins = {"http://localhost:4200", "http://tauri.localhost"})
public class VerfuegbarkeitController {

    private final VerfuegbarkeitService verfuegbarkeitService;
    private final MitgliedService mitgliedService;

    public VerfuegbarkeitController(
            VerfuegbarkeitService verfuegbarkeitService,
            MitgliedService mitgliedService
    ) {
        this.verfuegbarkeitService = verfuegbarkeitService;
        this.mitgliedService = mitgliedService;
    }

    @GetMapping
    public List<Verfuegbarkeit> alleVerfuegbarkeitenLaden() {
        return verfuegbarkeitService.alleVerfuegbarkeitenLaden();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Verfuegbarkeit> verfuegbarkeitNachIdLaden(
            @PathVariable Long id
    ) {
        return verfuegbarkeitService.verfuegbarkeitNachIdLaden(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Verfuegbarkeit> verfuegbarkeitAnlegen(
            @Valid @RequestBody VerfuegbarkeitCreateDto dto
    ) {

        Mitglied mitglied = mitgliedService
                .mitgliedNachIdLaden(dto.getMitgliedId())
                .orElse(null);

        if (mitglied == null) {
            return ResponseEntity.badRequest().build();
        }

        Verfuegbarkeit verfuegbarkeit = new Verfuegbarkeit();

        verfuegbarkeit.setDatum(dto.getDatum());
        verfuegbarkeit.setStartzeit(dto.getStartzeit());
        verfuegbarkeit.setEndzeit(dto.getEndzeit());
        verfuegbarkeit.setBemerkung(dto.getBemerkung());
        verfuegbarkeit.setMitglied(mitglied);

        Verfuegbarkeit gespeichert =
                verfuegbarkeitService.verfuegbarkeitSpeichern(
                        verfuegbarkeit
                );

        return ResponseEntity.ok(gespeichert);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Verfuegbarkeit> verfuegbarkeitBearbeiten(
            @PathVariable Long id,
            @Valid @RequestBody VerfuegbarkeitUpdateDto dto
    ) {

        return verfuegbarkeitService.verfuegbarkeitNachIdLaden(id)
                .map(vorhandeneVerfuegbarkeit -> {

                    Mitglied mitglied = mitgliedService
                            .mitgliedNachIdLaden(dto.getMitgliedId())
                            .orElse(null);

                    if (mitglied == null) {
                        return ResponseEntity
                                .badRequest()
                                .<Verfuegbarkeit>build();
                    }

                    vorhandeneVerfuegbarkeit.setDatum(
                            dto.getDatum()
                    );
                    vorhandeneVerfuegbarkeit.setStartzeit(
                            dto.getStartzeit()
                    );
                    vorhandeneVerfuegbarkeit.setEndzeit(
                            dto.getEndzeit()
                    );
                    vorhandeneVerfuegbarkeit.setBemerkung(
                            dto.getBemerkung()
                    );
                    vorhandeneVerfuegbarkeit.setMitglied(
                            mitglied
                    );

                    Verfuegbarkeit gespeichert =
                            verfuegbarkeitService
                                    .verfuegbarkeitSpeichern(
                                            vorhandeneVerfuegbarkeit
                                    );

                    return ResponseEntity.ok(gespeichert);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> verfuegbarkeitLoeschen(
            @PathVariable Long id
    ) {

        if (verfuegbarkeitService
                .verfuegbarkeitNachIdLaden(id)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        verfuegbarkeitService.verfuegbarkeitLoeschen(id);

        return ResponseEntity.noContent().build();
    }
}
