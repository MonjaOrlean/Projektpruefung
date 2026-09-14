# Testdokumentation – Vereins- und Eventplaner

## Testübersicht

| Test-ID | Testziel | Voraussetzungen | Testschritte | Erwartetes Ergebnis | Tatsächliches Ergebnis | Status | Fehler / Behebung |
|---|---|---|---|---|---|---|---|
| T01 | Mitglieder anlegen | Backend und Frontend laufen | Mitglied über die Oberfläche anlegen | Mitglied wird gespeichert und angezeigt | Mitglied wurde gespeichert und angezeigt | Bestanden | - |
| T02 | Mitglieder bearbeiten | Mitglied vorhanden | Mitglied bearbeiten und speichern | Änderungen werden übernommen | Änderungen wurden übernommen | Bestanden | - |
| T03 | Mitglieder löschen | Mitglied vorhanden | Mitglied löschen | Mitglied wird entfernt | Mitglied wurde entfernt | Bestanden | - |
| T04 | Veranstaltungsverwaltung | Backend und Frontend laufen | Veranstaltung anlegen, bearbeiten und löschen | Veranstaltung wird korrekt verarbeitet | Funktionierte wie erwartet | Bestanden | - |
| T05 | Ungültige Veranstaltungszeit | Veranstaltung anlegen | Endzeit vor Startzeit eintragen | Anfrage wird abgelehnt | HTTP 400 wurde zurückgegeben | Bestanden | Validierung funktioniert |
| T06 | Aufgabenverwaltung | Veranstaltung vorhanden | Aufgabe anlegen, bearbeiten, Status ändern, löschen | Aufgabe wird korrekt verarbeitet | Funktionierte wie erwartet | Bestanden | - |
| T07 | Schichtverwaltung | Veranstaltung vorhanden | Schicht anlegen, bearbeiten und löschen | Schicht wird korrekt verarbeitet | Funktionierte wie erwartet | Bestanden | - |
| T08 | Ungültige Schichtzeit | Schicht anlegen | Endzeit vor Startzeit eintragen | Anfrage wird abgelehnt | Anfrage wurde abgelehnt | Bestanden | - |
| T09 | Ungültige Personenanzahl | Schicht anlegen | Benötigte Personen kleiner als 1 eintragen | Anfrage wird abgelehnt | Anfrage wurde abgelehnt | Bestanden | - |
| T10 | Verfügbarkeiten | Mitglied vorhanden | Verfügbarkeit anlegen, bearbeiten und löschen | Verfügbarkeit wird gespeichert | Funktionierte wie erwartet | Bestanden | - |
| T11 | Schichtzuweisung | Mitglied, Schicht und Verfügbarkeit vorhanden | Mitglied einer Schicht zuweisen | Zuweisung wird gespeichert | Zuweisung wurde gespeichert | Bestanden | - |
| T12 | Doppelbuchung verhindern | Mitglied ist bereits überschneidend eingeplant | Zweite überschneidende Zuweisung anlegen | Zuweisung wird abgelehnt | HTTP 400 wurde zurückgegeben | Bestanden | Doppelbuchungsprüfung funktioniert |
| T13 | Unterbesetzung erkennen | Schicht benötigt mehr Personen als zugewiesen | Schichtstatus abrufen | Unterbesetzung wird angezeigt | Unterbesetzung wurde korrekt erkannt | Bestanden | - |
| T14 | Verfügbarkeitsprüfung | Mitglied ist nicht vollständig verfügbar | Mitglied einer Schicht zuweisen | Zuweisung wird abgelehnt | Anfrage wurde abgelehnt | Bestanden | - |
| T15 | Verfügbare Mitglieder filtern | Schicht vorhanden | Verfügbare Mitglieder abrufen | Nur verfügbare Mitglieder werden angeboten | Filterung funktioniert | Bestanden | - |
| T16 | Einsatzplan laden | Schichten und Zuweisungen vorhanden | Einsatzplan öffnen | Einsatzplan zeigt Daten korrekt an | Daten wurden korrekt angezeigt | Bestanden | - |
| T17 | CSV-Export | Einsatzplan vorhanden | Export ausführen | CSV-Datei wird erzeugt | Datei wurde erzeugt | Bestanden | - |
| T18 | Umlaute im CSV-Export | CSV mit Umlauten öffnen | Export in Excel öffnen | Umlaute werden korrekt dargestellt | Umlaute wurden nach UTF-8-BOM korrekt angezeigt | Bestanden | UTF-8-BOM ergänzt |
| T19 | Tauri-Entwicklungsstart | Rust, Node und Tauri installiert | `npx tauri dev` starten | Desktopfenster öffnet sich | Fenster wurde geöffnet | Bestanden | - |
| T20 | Mitglied in Tauri speichern | Tauri und Backend laufen | Mitglied in der Desktop-App anlegen | Mitglied wird gespeichert | Mitglied wurde gespeichert und war auch im Browser sichtbar | Bestanden | - |
| T21 | Persistenz nach Neustart | Mitglied gespeichert | Anwendung und Backend neu starten | Mitglied bleibt vorhanden | Mitglied war weiterhin vorhanden | Bestanden | - |
| T22 | Tauri Release-Build | Tauri-Konfiguration korrekt | `npx tauri build` ausführen | Release und Installer werden erzeugt | EXE, MSI und Setup wurden erzeugt | Bestanden | Fehler bei Bundle-ID und frontendDist zuvor korrigiert |
| T23 | Spring-Boot-JAR erstellen | Backend kompiliert | `.\mvnw clean package -DskipTests` ausführen | Ausführbare JAR wird erzeugt | JAR wurde erfolgreich erzeugt | Bestanden | - |
| T24 | Backend aus JAR starten | Release-JAR vorhanden | `java -jar ...` starten | Backend startet und Daten sind vorhanden | Backend startete und Mitglied war vorhanden | Bestanden | - |
| T25 | jpackage-App-Image | Java 17 und JAR vorhanden | Backend mit `jpackage` paketieren | EXE mit eingebetteter Runtime wird erzeugt | App-Image wurde erzeugt | Bestanden | - |
| T26 | Backend als jpackage-Anwendung starten | App-Image vorhanden | `VereinsplanerBackend.exe` starten | Backend startet ohne separaten Maven-Start | Backend startete und Mitglied war vorhanden | Bestanden | - |
| T27 | Automatischer Backend-Start durch Tauri | Backend als Resource eingebunden | Nur `npx tauri dev` starten | Tauri startet Backend automatisch | Backend wurde automatisch gestartet, Daten wurden geladen | Bestanden | - |
| T28 | Falsche Zuordnung von Veranstaltung und Einsatzbereich verhindern | Zwei Veranstaltungen mit unterschiedlichen Einsatzbereichen vorhanden | Schicht mit Veranstaltung 1 und Einsatzbereich 2 anlegen | Anfrage wird abgelehnt | HTTP 400 wurde zurückgegeben | Bestanden | Validierung im SchichtController funktioniert |
| T29 | Backend mit portabler jlink-Runtime starten | jlink-Runtime und aktuelle Backend-JAR vorhanden | Backend mit `runtime\bin\java.exe -jar ...` starten | Spring Boot startet ohne separat installiertes Java | Backend startete erfolgreich und Daten waren erreichbar | Bestanden | jpackage-Launcher verworfen, portable jlink-Runtime verwendet |
| T30 | Automatischer Backend-Start über Tauri mit portabler jlink-Runtime | Tauri-Ressourcen enthalten Runtime und Backend-JAR | `npx tauri dev` starten und `http://localhost:8080/api/mitglieder` aufrufen | Backend startet automatisch und API antwortet | Backend wurde automatisch gestartet, API lieferte `[]` | Bestanden | Alter jpackage-Launcher wurde durch portable jlink-Runtime ersetzt |
## Offene Tests

- Einsatzbereich einer Veranstaltung zuordnen
- Schicht einem Einsatzbereich zuordnen
- Ungültige Kombination aus Veranstaltung und Einsatzbereich ablehnen
- Löschen von Mitgliedern mit abhängigen Daten
- Löschen von Veranstaltungen mit abhängigen Daten
- Löschen von Einsatzbereichen mit verwendeten Schichten
- Finalen Installer auf sauberem Testsystem prüfen
- Desktop-Verknüpfung prüfen
- Anwendung per Doppelklick vollständig starten
- Sauberes Beenden von Tauri und Backend prüfen