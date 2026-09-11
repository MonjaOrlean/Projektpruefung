package de.vereinsplaner.backend.controller;

import de.vereinsplaner.backend.dto.EinsatzplanSchichtDto;
import de.vereinsplaner.backend.service.EinsatzplanService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/einsatzplan")
@CrossOrigin(origins = "http://localhost:4200")
public class EinsatzplanController {

    private final EinsatzplanService einsatzplanService;

    public EinsatzplanController(
            EinsatzplanService einsatzplanService
    ) {
        this.einsatzplanService = einsatzplanService;
    }

    @GetMapping
    public ResponseEntity<List<EinsatzplanSchichtDto>> einsatzplanLaden() {

        List<EinsatzplanSchichtDto> einsatzplan =
                einsatzplanService.einsatzplanLaden();

        return ResponseEntity.ok(einsatzplan);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> einsatzplanExportieren() {

        List<EinsatzplanSchichtDto> einsatzplan =
                einsatzplanService.einsatzplanLaden();

        StringBuilder csv = new StringBuilder();

        /*
         * UTF-8 BOM:
         * Damit Excel Umlaute wie ä, ö und ü
         * korrekt erkennt.
         */
        csv.append('\uFEFF');

        csv.append(
                "Schicht;Datum;Startzeit;Endzeit;Veranstaltung;"
        );

        csv.append(
                "Benötigt;Zugewiesen;Fehlend;Status;Mitglieder\n"
        );

        for (EinsatzplanSchichtDto schicht : einsatzplan) {

            csv.append(
                    csvWert(schicht.getSchichtName())
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            String.valueOf(
                                    schicht.getDatum()
                            )
                    )
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            String.valueOf(
                                    schicht.getStartzeit()
                            )
                    )
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            String.valueOf(
                                    schicht.getEndzeit()
                            )
                    )
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            schicht.getVeranstaltungName()
                    )
            );
            csv.append(";");

            csv.append(
                    schicht.getBenoetigtePersonen()
            );
            csv.append(";");

            csv.append(
                    schicht.getZugewiesenePersonen()
            );
            csv.append(";");

            csv.append(
                    schicht.getFehlendePersonen()
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            schicht.isUnterbesetzt()
                                    ? "Unterbesetzt"
                                    : "Ausreichend besetzt"
                    )
            );
            csv.append(";");

            csv.append(
                    csvWert(
                            String.join(
                                    ", ",
                                    schicht.getMitglieder()
                            )
                    )
            );

            csv.append("\n");
        }

        byte[] dateiInhalt =
                csv.toString()
                        .getBytes(
                                StandardCharsets.UTF_8
                        );

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"einsatzplan.csv\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                "text/csv; charset=UTF-8"
                        )
                )
                .body(dateiInhalt);
    }

    private String csvWert(String wert) {

        if (wert == null) {
            return "";
        }

        String bereinigt =
                wert.replace(
                        "\"",
                        "\"\""
                );

        return "\"" + bereinigt + "\"";
    }
}