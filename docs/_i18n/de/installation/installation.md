# Installation und Update

> Die Anweisungen zur Installation und Update von PSA richtet sich an Laien. Es kann sein, dass Sie bestimmte Schritte bereits vorgenommen haben oder gar nicht auf eine Anweisung angewiessen sind.

## Wie installiere ich PSA

### auf einem macOS

**Schritte**

1. Laden Sie sich die neuste Version von [PSA](https://github.com/BilledTrain380/sporttag-psa/releases) herunter.
Das Asset welches Sie benötigen folgt dem Namensschema `PSA-<version>.dmg`, wobei `<version>` die jeweilige PSA Version ist.
z.B. `PSA-2.0.0.dmg`

2. Doppelklicken Sie das heruntergeladene dmg und folgen Sie den Anweisungen.

Das App befindet sich danach in Ihrem Program Ordner. Starten Sie einfach das Program und
die Administrations Oberfläche von PSA öffnet sich.

### auf einem Windows

TODO: Add steps for Windows installation

### als Java App

**Voraussetzungen**
* Sie sind mit CLI vertraut
* Sie haben Java 1.8 oder höher auf Ihrem System installiert

**Schritte**

1. Laden Sie sich die neuste Version von [PSA](https://github.com/BilledTrain380/sporttag-psa/releases) herunter.
Das Asset welches Sie benötigen folgt dem Namensschema `PSA-<version>_Java.zip`, wobei `<version>` die jeweilige PSA Version ist.
z.B. `PSA-2.0.0_Java.zip`

2. Enpacken Sie das Archiv

3. Kopieren Sie die Java Datei in ein Verzeichnis Ihrer Wahl.

4. Starten Sie das Program per Command Line Befehl: `java -jar PSA-<version>.jar`

## Wie update ich PSA

### auf einem macOS

**Voraussetzungen**
* Schliessen Sie PSA

**Schritte**

1. Laden Sie sich die neue Version von [PSA](https://github.com/BilledTrain380/sporttag-psa/releases) herunter.
Das Asset welches Sie benötigen folgt dem Namensschema `PSA-<version>.dmg`, wobei `<version>` die jeweilige PSA Version ist.
z.B. `PSA-2.0.0.dmg`

2. Doppelklicken Sie das heruntergeladene dmg.

3. Überschreiben Sie das bestehende `PSA.app` Program mit dem neuen.

Beim nächsten Start wird PSA automatisch eine Migration Ihrer bestehenden Daten vornehmen. Sie brauchen sich
um nichts weiter zu kümmern.

### auf einem Windows

TODO: Add steps for Windows Update

### als Java App

1. Laden Sie sich die neue Version von [PSA](https://github.com/BilledTrain380/sporttag-psa/releases) herunter.
Das Asset welches Sie benötigen folgt dem Namensschema `PSA-<version>_Java.zip`, wobei `<version>` die jeweilige PSA Version ist.
z.B. `PSA-2.0.0_Java.zip`

2. Enpacken Sie das Archiv

3. Überschreiben Sie Ihre alte Java Datei mit der neuen.

Beim nächsten Start wird PSA automatisch eine Migration Ihrer bestehenden Daten vornehmen. Sie brauchen sich
um nichts weiter zu kümmern.