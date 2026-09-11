package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.dto.EinsatzplanSchichtDto;
import de.vereinsplaner.backend.model.Schicht;
import de.vereinsplaner.backend.model.SchichtZuweisung;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EinsatzplanService {

    private final SchichtService schichtService;
    private final SchichtZuweisungService schichtZuweisungService;

    public EinsatzplanService(
            SchichtService schichtService,
            SchichtZuweisungService schichtZuweisungService
    ) {
        this.schichtService = schichtService;
        this.schichtZuweisungService = schichtZuweisungService;
    }

    public List<EinsatzplanSchichtDto> einsatzplanLaden() {

        List<Schicht> schichten =
                schichtService.alleSchichtenLaden();

        List<EinsatzplanSchichtDto> einsatzplan =
                new ArrayList<>();

        for (Schicht schicht : schichten) {

            if (schicht.getId() == null) {
                continue;
            }

            List<SchichtZuweisung> zuweisungen =
                    schichtZuweisungService
                            .zuweisungenNachSchichtLaden(
                                    schicht.getId()
                            );

            List<String> mitglieder =
                    new ArrayList<>();

            for (SchichtZuweisung zuweisung : zuweisungen) {

                if (zuweisung.getMitglied() == null) {
                    continue;
                }

                String name =
                        zuweisung.getMitglied().getVorname()
                                + " "
                                + zuweisung.getMitglied().getNachname();

                mitglieder.add(name);
            }

            int zugewiesenePersonen =
                    zuweisungen.size();

            int fehlendePersonen =
                    Math.max(
                            schicht.getBenoetigtePersonen()
                                    - zugewiesenePersonen,
                            0
                    );

            boolean unterbesetzt =
                    zugewiesenePersonen
                            < schicht.getBenoetigtePersonen();

            String veranstaltungName = null;

            if (schicht.getVeranstaltung() != null) {
                veranstaltungName =
                        schicht.getVeranstaltung().getName();
            }

            EinsatzplanSchichtDto dto =
                    new EinsatzplanSchichtDto(
                            schicht.getId(),
                            schicht.getName(),
                            schicht.getDatum(),
                            schicht.getStartzeit(),
                            schicht.getEndzeit(),
                            veranstaltungName,
                            schicht.getBenoetigtePersonen(),
                            zugewiesenePersonen,
                            fehlendePersonen,
                            unterbesetzt,
                            mitglieder
                    );

            einsatzplan.add(dto);
        }

        return einsatzplan;
    }
}