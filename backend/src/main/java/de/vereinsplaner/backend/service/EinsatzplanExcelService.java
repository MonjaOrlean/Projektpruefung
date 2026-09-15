package de.vereinsplaner.backend.service;

import de.vereinsplaner.backend.dto.EinsatzplanSchichtDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EinsatzplanExcelService {

    public Path einsatzplanAlsExcelSpeichern(
            List<EinsatzplanSchichtDto> schichten
    ) throws IOException {

        Path downloadOrdner =
                Paths.get(
                        System.getProperty("user.home"),
                        "Downloads"
                );

        Files.createDirectories(downloadOrdner);

        String zeitstempel =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd_HH-mm"
                                )
                        );

        Path datei =
                downloadOrdner.resolve(
                        "Einsatzplan_" +
                                zeitstempel +
                                ".xlsx"
                );

        try (
                XSSFWorkbook workbook =
                        new XSSFWorkbook();

                OutputStream outputStream =
                        Files.newOutputStream(datei)
        ) {

            Map<String, List<EinsatzplanSchichtDto>>
                    nachVeranstaltung =
                    schichten.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            schicht ->
                                                    schicht.getVeranstaltungName()
                                                            == null
                                                            ? "Ohne Veranstaltung"
                                                            : schicht.getVeranstaltungName(),
                                            LinkedHashMap::new,
                                            Collectors.toList()
                                    )
                            );

            if (nachVeranstaltung.isEmpty()) {

                XSSFSheet sheet =
                        workbook.createSheet(
                                "Einsatzplan"
                        );

                Row row =
                        sheet.createRow(0);

                row.createCell(0)
                        .setCellValue(
                                "Keine Schichten vorhanden."
                        );

            } else {

                for (
                        Map.Entry<
                                String,
                                List<EinsatzplanSchichtDto>
                                > eintrag :
                        nachVeranstaltung.entrySet()
                ) {

                    erstelleVeranstaltungsblatt(
                            workbook,
                            eintrag.getKey(),
                            eintrag.getValue()
                    );
                }
            }

            workbook.write(outputStream);
        }

        return datei;
    }

    private void erstelleVeranstaltungsblatt(
            XSSFWorkbook workbook,
            String veranstaltung,
            List<EinsatzplanSchichtDto> schichten
    ) {

        XSSFSheet sheet =
                workbook.createSheet(
                        gueltigerTabellenName(
                                veranstaltung
                        )
                );

        sheet.setDisplayGridlines(false);

        List<String> einsatzbereiche =
                schichten.stream()
                        .map(
                                schicht ->
                                        schicht.getEinsatzbereichName()
                                                == null
                                                ? "Ohne Einsatzbereich"
                                                : schicht.getEinsatzbereichName()
                        )
                        .distinct()
                        .sorted()
                        .toList();

        Set<String> mitgliederSet =
                new TreeSet<>();

        for (
                EinsatzplanSchichtDto schicht :
                schichten
        ) {

            mitgliederSet.addAll(
                    schicht.getMitglieder()
            );
        }

        List<String> mitglieder =
                new ArrayList<>(
                        mitgliederSet
                );

        int anzahlSpalten =
                Math.max(
                        einsatzbereiche.size() + 1,
                        8
                );

        XSSFCellStyle titelStyle =
                titelStyle(workbook);

        XSSFCellStyle untertitelStyle =
                untertitelStyle(workbook);

        XSSFCellStyle kopfStyle =
                kopfStyle(workbook);

        XSSFCellStyle mitgliedStyle =
                mitgliedStyle(workbook);

        XSSFCellStyle matrixStyle =
                matrixStyle(workbook);

        XSSFCellStyle matrixAlternativStyle =
                matrixAlternativStyle(workbook);

        XSSFCellStyle normalStyle =
                normalStyle(workbook);

        XSSFCellStyle normalAlternativStyle =
                normalAlternativStyle(workbook);

        XSSFCellStyle gruenStyle =
                statusStyle(
                        workbook,
                        214,
                        242,
                        220,
                        31,
                        108,
                        55
                );

        XSSFCellStyle orangeStyle =
                statusStyle(
                        workbook,
                        255,
                        237,
                        194,
                        142,
                        96,
                        0
                );

        XSSFCellStyle rotStyle =
                statusStyle(
                        workbook,
                        255,
                        220,
                        220,
                        166,
                        0,
                        0
                );

        int zeileIndex = 0;

        /*
         * TITEL
         */

        Row titelZeile =
                sheet.createRow(
                        zeileIndex++
                );

        titelZeile.setHeightInPoints(
                34
        );

        Cell titelZelle =
                titelZeile.createCell(0);

        titelZelle.setCellValue(
                "EINSATZPLAN"
        );

        titelZelle.setCellStyle(
                titelStyle
        );

        sheet.addMergedRegion(
                new CellRangeAddress(
                        0,
                        0,
                        0,
                        anzahlSpalten - 1
                )
        );

        /*
         * VERANSTALTUNG
         */

        Row veranstaltungsZeile =
                sheet.createRow(
                        zeileIndex++
                );

        veranstaltungsZeile.setHeightInPoints(
                26
        );

        Cell veranstaltungsZelle =
                veranstaltungsZeile.createCell(0);

        veranstaltungsZelle.setCellValue(
                veranstaltung
        );

        veranstaltungsZelle.setCellStyle(
                untertitelStyle
        );

        sheet.addMergedRegion(
                new CellRangeAddress(
                        1,
                        1,
                        0,
                        anzahlSpalten - 1
                )
        );

        /*
         * INFOZEILE
         */

        Row infoZeile =
                sheet.createRow(
                        zeileIndex++
                );

        Cell infoZelle =
                infoZeile.createCell(0);

        infoZelle.setCellValue(
                "Erstellt am: " +
                        LocalDateTime.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd.MM.yyyy HH:mm"
                                        )
                                )
        );

        XSSFCellStyle infoStyle =
                workbook.createCellStyle();

        XSSFFont infoFont =
                workbook.createFont();

        infoFont.setItalic(true);
        infoFont.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 100,
                                (byte) 100,
                                (byte) 100
                        },
                        null
                )
        );

        infoStyle.setFont(
                infoFont
        );

        infoZelle.setCellStyle(
                infoStyle
        );

        zeileIndex++;

        /*
         * MATRIXÜBERSCHRIFT
         */

        Row matrixTitel =
                sheet.createRow(
                        zeileIndex++
                );

        Cell matrixTitelCell =
                matrixTitel.createCell(0);

        matrixTitelCell.setCellValue(
                "Mitglieder und Einsatzbereiche"
        );

        matrixTitelCell.setCellStyle(
                abschnittTitelStyle(
                        workbook
                )
        );

        /*
         * MATRIXKOPF
         */

        Row matrixKopf =
                sheet.createRow(
                        zeileIndex++
                );

        matrixKopf.setHeightInPoints(
                28
        );

        Cell mitgliedKopf =
                matrixKopf.createCell(0);

        mitgliedKopf.setCellValue(
                "Mitglied"
        );

        mitgliedKopf.setCellStyle(
                kopfStyle
        );

        for (
                int i = 0;
                i < einsatzbereiche.size();
                i++
        ) {

            Cell zelle =
                    matrixKopf.createCell(
                            i + 1
                    );

            zelle.setCellValue(
                    einsatzbereiche.get(i)
            );

            zelle.setCellStyle(
                    kopfStyle
            );
        }

        /*
         * MATRIXINHALT
         */

        if (mitglieder.isEmpty()) {

            Row leerZeile =
                    sheet.createRow(
                            zeileIndex++
                    );

            leerZeile.setHeightInPoints(
                    28
            );

            Cell leer =
                    leerZeile.createCell(0);

            leer.setCellValue(
                    "Noch keine Mitglieder zugewiesen."
            );

            leer.setCellStyle(
                    normalStyle
            );

        } else {

            int mitgliedZaehler = 0;

            for (
                    String mitglied :
                    mitglieder
            ) {

                Row row =
                        sheet.createRow(
                                zeileIndex++
                        );

                row.setHeightInPoints(
                        36
                );

                boolean alternativ =
                        mitgliedZaehler % 2 == 1;

                Cell nameCell =
                        row.createCell(0);

                nameCell.setCellValue(
                        mitglied
                );

                nameCell.setCellStyle(
                        mitgliedStyle
                );

                for (
                        int bereichIndex = 0;
                        bereichIndex <
                                einsatzbereiche.size();
                        bereichIndex++
                ) {

                    String bereich =
                            einsatzbereiche.get(
                                    bereichIndex
                            );

                    List<EinsatzplanSchichtDto>
                            passendeSchichten =
                            schichten.stream()
                                    .filter(
                                            schicht -> {

                                                String schichtBereich =
                                                        schicht.getEinsatzbereichName();

                                                if (
                                                        schichtBereich
                                                                == null
                                                ) {
                                                    schichtBereich =
                                                            "Ohne Einsatzbereich";
                                                }

                                                return schichtBereich
                                                        .equals(
                                                                bereich
                                                        )
                                                        &&
                                                        schicht
                                                                .getMitglieder()
                                                                .contains(
                                                                        mitglied
                                                                );
                                            }
                                    )
                                    .toList();

                    Cell zelle =
                            row.createCell(
                                    bereichIndex + 1
                            );

                    if (
                            passendeSchichten
                                    .isEmpty()
                    ) {

                        zelle.setCellValue(
                                "–"
                        );

                    } else {

                        String text =
                                passendeSchichten
                                        .stream()
                                        .map(
                                                schicht ->
                                                        schicht.getStartzeit()
                                                                +
                                                                " – "
                                                                +
                                                                schicht.getEndzeit()
                                                                +
                                                                "\n✓ "
                                                                +
                                                                schicht.getSchichtName()
                                        )
                                        .collect(
                                                Collectors.joining(
                                                        "\n"
                                                )
                                        );

                        zelle.setCellValue(
                                text
                        );
                    }

                    zelle.setCellStyle(
                            alternativ
                                    ? matrixAlternativStyle
                                    : matrixStyle
                    );
                }

                mitgliedZaehler++;
            }
        }

        zeileIndex += 2;

        /*
         * BESETZUNGSÜBERSCHRIFT
         */

        Row statusTitel =
                sheet.createRow(
                        zeileIndex++
                );

        Cell statusTitelZelle =
                statusTitel.createCell(0);

        statusTitelZelle.setCellValue(
                "Besetzungsübersicht"
        );

        statusTitelZelle.setCellStyle(
                abschnittTitelStyle(
                        workbook
                )
        );

        /*
         * STATUS KOPFZEILE
         */

        Row statusKopf =
                sheet.createRow(
                        zeileIndex++
                );

        statusKopf.setHeightInPoints(
                28
        );

        String[] statusSpalten = {
                "Einsatzbereich",
                "Schicht",
                "Datum",
                "Zeit",
                "Benötigt",
                "Zugewiesen",
                "Fehlend",
                "Status"
        };

        for (
                int i = 0;
                i < statusSpalten.length;
                i++
        ) {

            Cell cell =
                    statusKopf.createCell(i);

            cell.setCellValue(
                    statusSpalten[i]
            );

            cell.setCellStyle(
                    kopfStyle
            );
        }

        /*
         * STATUSDATEN
         */

        int statusZaehler = 0;

        for (
                EinsatzplanSchichtDto schicht :
                schichten
        ) {

            Row row =
                    sheet.createRow(
                            zeileIndex++
                    );

            row.setHeightInPoints(
                    28
            );

            boolean alternativ =
                    statusZaehler % 2 == 1;

            XSSFCellStyle zeilenStyle =
                    alternativ
                            ? normalAlternativStyle
                            : normalStyle;

            String bereich =
                    schicht
                            .getEinsatzbereichName();

            if (bereich == null) {

                bereich =
                        "Ohne Einsatzbereich";
            }

            Cell bereichCell =
                    row.createCell(0);

            bereichCell.setCellValue(
                    bereich
            );

            bereichCell.setCellStyle(
                    zeilenStyle
            );

            Cell schichtCell =
                    row.createCell(1);

            schichtCell.setCellValue(
                    schicht.getSchichtName()
            );

            schichtCell.setCellStyle(
                    zeilenStyle
            );

            Cell datumCell =
                    row.createCell(2);

            datumCell.setCellValue(
                    String.valueOf(
                            schicht.getDatum()
                    )
            );

            datumCell.setCellStyle(
                    zeilenStyle
            );

            Cell zeitCell =
                    row.createCell(3);

            zeitCell.setCellValue(
                    schicht.getStartzeit()
                            +
                            " – "
                            +
                            schicht.getEndzeit()
            );

            zeitCell.setCellStyle(
                    zeilenStyle
            );

            Cell benoetigtCell =
                    row.createCell(4);

            benoetigtCell.setCellValue(
                    schicht
                            .getBenoetigtePersonen()
            );

            benoetigtCell.setCellStyle(
                    zeilenStyle
            );

            Cell zugewiesenCell =
                    row.createCell(5);

            zugewiesenCell.setCellValue(
                    schicht
                            .getZugewiesenePersonen()
            );

            zugewiesenCell.setCellStyle(
                    zeilenStyle
            );

            Cell fehlendCell =
                    row.createCell(6);

            fehlendCell.setCellValue(
                    schicht
                            .getFehlendePersonen()
            );

            fehlendCell.setCellStyle(
                    zeilenStyle
            );

            Cell statusCell =
                    row.createCell(7);

            if (
                    schicht
                            .getZugewiesenePersonen()
                            == 0
            ) {

                statusCell.setCellValue(
                        "✕ Nicht besetzt"
                );

                statusCell.setCellStyle(
                        rotStyle
                );

            } else if (
                    schicht.isUnterbesetzt()
            ) {

                statusCell.setCellValue(
                        "⚠ Unterbesetzt"
                );

                statusCell.setCellStyle(
                        orangeStyle
                );

            } else {

                statusCell.setCellValue(
                        "✓ Vollständig"
                );

                statusCell.setCellStyle(
                        gruenStyle
                );
            }

            statusZaehler++;
        }

        /*
         * LEGENDE
         */

        zeileIndex += 2;

        Row legende =
                sheet.createRow(
                        zeileIndex
                );

        Cell legendeTitel =
                legende.createCell(0);

        legendeTitel.setCellValue(
                "Status:"
        );

        legendeTitel.setCellStyle(
                normalStyle
        );

        Cell legendeGruen =
                legende.createCell(1);

        legendeGruen.setCellValue(
                "✓ Vollständig"
        );

        legendeGruen.setCellStyle(
                gruenStyle
        );

        Cell legendeOrange =
                legende.createCell(2);

        legendeOrange.setCellValue(
                "⚠ Unterbesetzt"
        );

        legendeOrange.setCellStyle(
                orangeStyle
        );

        Cell legendeRot =
                legende.createCell(3);

        legendeRot.setCellValue(
                "✕ Nicht besetzt"
        );

        legendeRot.setCellStyle(
                rotStyle
        );

        /*
         * SPALTENBREITEN
         */

        sheet.setColumnWidth(
                0,
                26 * 256
        );

        for (
                int i = 1;
                i < anzahlSpalten;
                i++
        ) {

            sheet.setColumnWidth(
                    i,
                    22 * 256
            );
        }

        sheet.setColumnWidth(
                1,
                26 * 256
        );

        sheet.setColumnWidth(
                2,
                14 * 256
        );

        sheet.setColumnWidth(
                3,
                18 * 256
        );

        sheet.setColumnWidth(
                4,
                12 * 256
        );

        sheet.setColumnWidth(
                5,
                14 * 256
        );

        sheet.setColumnWidth(
                6,
                12 * 256
        );

        sheet.setColumnWidth(
                7,
                22 * 256
        );

        /*
         * DRUCKEINSTELLUNGEN
         */

        sheet.setFitToPage(true);

        PrintSetup printSetup =
                sheet.getPrintSetup();

        printSetup.setLandscape(true);
        printSetup.setFitWidth(
                (short) 1
        );
        printSetup.setFitHeight(
                (short) 0
        );

        sheet.setMargin(
                Sheet.LeftMargin,
                0.3
        );

        sheet.setMargin(
                Sheet.RightMargin,
                0.3
        );

        sheet.setMargin(
                Sheet.TopMargin,
                0.5
        );

        sheet.setMargin(
                Sheet.BottomMargin,
                0.5
        );

        /*
         * MATRIX-KOPF FESTHALTEN
         */

        sheet.createFreezePane(
                1,
                6
        );
    }

    private XSSFCellStyle titelStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        font.setFontHeightInPoints(
                (short) 22
        );

        font.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 255,
                                (byte) 255,
                                (byte) 255
                        },
                        null
                )
        );

        style.setFont(font);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 47,
                                (byte) 125,
                                (byte) 50
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        style.setAlignment(
                HorizontalAlignment.LEFT
        );

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        return style;
    }

    private XSSFCellStyle untertitelStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        font.setFontHeightInPoints(
                (short) 15
        );

        font.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 31,
                                (byte) 78,
                                (byte) 34
                        },
                        null
                )
        );

        style.setFont(font);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 226,
                                (byte) 239,
                                (byte) 218
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        return style;
    }

    private XSSFCellStyle abschnittTitelStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        font.setFontHeightInPoints(
                (short) 13
        );

        font.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 31,
                                (byte) 78,
                                (byte) 34
                        },
                        null
                )
        );

        style.setFont(font);

        return style;
    }

    private XSSFCellStyle kopfStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        font.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 255,
                                (byte) 255,
                                (byte) 255
                        },
                        null
                )
        );

        style.setFont(font);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 70,
                                (byte) 110,
                                (byte) 73
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        style.setAlignment(
                HorizontalAlignment.CENTER
        );

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        style.setWrapText(true);

        rahmenSetzen(style);

        return style;
    }

    private XSSFCellStyle mitgliedStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                normalStyle(workbook);

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        style.setFont(font);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 242,
                                (byte) 247,
                                (byte) 242
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        return style;
    }

    private XSSFCellStyle matrixStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                normalStyle(workbook);

        style.setAlignment(
                HorizontalAlignment.CENTER
        );

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        style.setWrapText(true);

        return style;
    }

    private XSSFCellStyle matrixAlternativStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                matrixStyle(workbook);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 248,
                                (byte) 250,
                                (byte) 248
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        return style;
    }

    private XSSFCellStyle normalStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        style.setWrapText(true);

        rahmenSetzen(style);

        return style;
    }

    private XSSFCellStyle normalAlternativStyle(
            XSSFWorkbook workbook
    ) {

        XSSFCellStyle style =
                normalStyle(workbook);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) 248,
                                (byte) 250,
                                (byte) 248
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        return style;
    }

    private XSSFCellStyle statusStyle(
            XSSFWorkbook workbook,
            int rot,
            int gruen,
            int blau,
            int textRot,
            int textGruen,
            int textBlau
    ) {

        XSSFCellStyle style =
                workbook.createCellStyle();

        XSSFFont font =
                workbook.createFont();

        font.setBold(true);

        font.setColor(
                new XSSFColor(
                        new byte[]{
                                (byte) textRot,
                                (byte) textGruen,
                                (byte) textBlau
                        },
                        null
                )
        );

        style.setFont(font);

        style.setFillForegroundColor(
                new XSSFColor(
                        new byte[]{
                                (byte) rot,
                                (byte) gruen,
                                (byte) blau
                        },
                        null
                )
        );

        style.setFillPattern(
                FillPatternType.SOLID_FOREGROUND
        );

        style.setAlignment(
                HorizontalAlignment.CENTER
        );

        style.setVerticalAlignment(
                VerticalAlignment.CENTER
        );

        style.setWrapText(true);

        rahmenSetzen(style);

        return style;
    }

    private void rahmenSetzen(
            CellStyle style
    ) {

        style.setBorderTop(
                BorderStyle.THIN
        );

        style.setBorderBottom(
                BorderStyle.THIN
        );

        style.setBorderLeft(
                BorderStyle.THIN
        );

        style.setBorderRight(
                BorderStyle.THIN
        );

        style.setTopBorderColor(
                IndexedColors.GREY_25_PERCENT
                        .getIndex()
        );

        style.setBottomBorderColor(
                IndexedColors.GREY_25_PERCENT
                        .getIndex()
        );

        style.setLeftBorderColor(
                IndexedColors.GREY_25_PERCENT
                        .getIndex()
        );

        style.setRightBorderColor(
                IndexedColors.GREY_25_PERCENT
                        .getIndex()
        );
    }

    private String gueltigerTabellenName(
            String name
    ) {

        String bereinigt =
                name.replaceAll(
                        "[\\\\/*?:\\[\\]]",
                        "_"
                );

        if (
                bereinigt.length() > 31
        ) {

            bereinigt =
                    bereinigt.substring(
                            0,
                            31
                    );
        }

        if (
                bereinigt.isBlank()
        ) {

            return "Einsatzplan";
        }

        return bereinigt;
    }
}