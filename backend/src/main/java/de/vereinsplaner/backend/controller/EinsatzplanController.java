package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.EinsatzplanSchichtDto;
import de.vereinsplaner.backend.service.EinsatzplanExcelService;
import de.vereinsplaner.backend.service.EinsatzplanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/einsatzplan")
@CrossOrigin(origins = {"http://localhost:4200", "http://tauri.localhost"})
public class EinsatzplanController {

    private final EinsatzplanService einsatzplanService;

    private final EinsatzplanExcelService einsatzplanExcelService;

    public EinsatzplanController(
            EinsatzplanService einsatzplanService,
            EinsatzplanExcelService einsatzplanExcelService
    ) {
        this.einsatzplanService = einsatzplanService;
        this.einsatzplanExcelService = einsatzplanExcelService;
    }

    @GetMapping
    public ResponseEntity<List<EinsatzplanSchichtDto>> einsatzplanLaden() {

        List<EinsatzplanSchichtDto> einsatzplan =
                einsatzplanService.einsatzplanLaden();

        return ResponseEntity.ok(einsatzplan);
    }

    @GetMapping("/export")
    public ResponseEntity<String> einsatzplanExportieren() {

        try {

            List<EinsatzplanSchichtDto> einsatzplan =
                    einsatzplanService.einsatzplanLaden();

            Path datei =
                    einsatzplanExcelService
                            .einsatzplanAlsExcelSpeichern(
                                    einsatzplan
                            );

            return ResponseEntity.ok(
                    datei.toAbsolutePath().toString()
            );

        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Der Einsatzplan konnte nicht exportiert werden."
                    );
        }
    }
}
