# Museumsvirtualisierung
Projekt von Pascal Krause und Dominik Dosdall, Studenten der Hochschule Harz, im Anwendungspraktikum Digitales Kulturerbe / Internet of Things

# externer Rechner
Erstellen sie aus den Java-Projekten ausführbare Jar-Dateien.
Die Projekte der Jar-Dateien sind in dem Verzeichnis "/Kommunikation-S3-Bucket-Projekte" hinterlegt.
Für die Bildrekonstruktion mit Meshroom wird eine Nvidia Grafikkarte benötigt. Daher wird ein externer Rechner empfohlen.
Damit man auf jedem Rechner AWS-Sumerian mit den eigenen Objekten nutzen kann, muss man diese Objekte aus dem S3-Bucket herunterladen und in Sumerian hochladen.

# Meshroom
Alle Dateien des Meshroom-Projektes finden Sie unter "https://github.com/AliceVision/meshroom".

# Raspberry Pi
Die aus S3-Upload entstandene Jar-Datei dient dem Upload der Bilder vom Raspberry Pi in den S3-Bucket.
Die Datei KameraAufnahme.py startet das Skript zum Aufnehmen von Bildern durch die angeschlossene Kamera.

# Jars
Die Jars erzeugen beim Ausführen Konfigurationsdateien, die vom Nutzer ausgefüllt werden müssen.
Um eine Verbindung zum eigenen AWS-Account herstellen zu können, bedarf es der AWS-Credentials. Diese werden unter "C:\Users\\@Nutzername\.aws" gespeichert.
Die Daten für die Datei findet man im AWS-Account. Der Credentials String muss einfach in die Datei hineinkopiert werden.
