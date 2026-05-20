# Quizduell

Ein rundenbasiertes 1-vs-1-Quizspiel in Java, inspiriert vom klassischen Quizduell-Spielprinzip. Zwei Spieler treten in bis zu 6 Runden gegeneinander an, wählen Kategorien und beantworten Multiple-Choice-Fragen.

## Features

- **Client-Server-Architektur** über TCP-Sockets (Port 12345)
- **Swing-GUI** mit CardLayout für nahtlose Panel-Übergänge
- **Lobby-System** mit 4-stelligen Lobby-Codes (Host/Join)
- **6 Runden** mit abwechselnder Kategoriewahl per Kategorienrad
- **Multiple-Choice-Fragen** (A, B, C, D) aus XML-Fragedatenbanken
- **Echtzeit-Spielzustand** – beide Spieler sehen nach jeder Runde den aktuellen Punktestand
- **Spielergebnis** mit Statistikanzeige am Spielende
- **XML-basierte Fragedatenbank** (JDOM2, DTD-validiert) – eigene Quiz-Sets erstellbar

## Projektstruktur

```
src/
├── client/          – GUI-Einstieg, ClientHandler (Netzwerkkommunikation), GameManager (Spiellogik clientseitig)
│   └── gui/         – Swing-Panels (Lobby, Kategorienrad, Fragen, Statistik, Quiz-Erstellung)
├── server/          – Server, Player-Threads, QuizGame, GameState, QuizReader
├── protocol/        – Gemeinsame Datenklassen (Category, Question, GameOutcome) und alle Message-Typen
└── utils/           – Hilfsfunktionen (z. B. Zufalls-Benutzernamen)
resources/
└── quiz.xml         – Fragedatenbank (Kategorien, Fragen, Lösungen)
```

## Protokoll

Die Kommunikation zwischen Client und Server erfolgt über serialisierte `Message`-Objekte:

| Richtung        | Nachricht              | Bedeutung                        |
|-----------------|------------------------|----------------------------------|
| Server → Client | `LOGIN`                | Benutzername & UUID vergeben     |
| Server → Client | `HOSTED_LOBBY`         | Lobby erstellt, Code mitgeteilt  |
| Server → Client | `START_GAME`           | Beide Spieler verbunden          |
| Server → Client | `PLAYER_TURN`          | Zugwechsel mit aktuellem State   |
| Server → Client | `SEND_CATEGORIES`      | Kategorieauswahl anfordern       |
| Server → Client | `UPDATE_GAME`          | Spielzustand nach Runde          |
| Server → Client | `GAME_OVER`            | Spielende mit Ergebnis           |
| Client → Server | `HOST_LOBBY`           | Lobby erstellen                  |
| Client → Server | `JOIN_LOBBY`           | Lobby beitreten                  |
| Client → Server | `SELECT_CATEGORY`      | Kategorie gewählt                |
| Client → Server | `ANSWER_QUESTION`      | Antwort abgeben                  |
| Client → Server | `SEND_QUIZSET`         | Eigenes Quiz-Set hochladen       |

## Fragedatenbank

Fragen werden aus XML-Dateien im Verzeichnis `resources/` geladen und müssen dem Schema in `quiz.dtd` entsprechen. Die Struktur umfasst `<Kategorien>`, `<Fragen>` und `<Lösungen>`.

## Farbschema

| Farbe     | Hex       |
|-----------|-----------|
| Hintergrund (dunkel) | `#44624a` |
| Hintergrund (mittel) | `#c0cfb2` |
| Hintergrund (hell)   | `#f1ebe1` |

## Abhängigkeiten

- Java (JDK 11+)
- [JDOM2](http://www.jdom.org/) – XML-Parsing
