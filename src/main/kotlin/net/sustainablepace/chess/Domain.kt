package net.sustainablepace.chess

typealias Schach = Boolean
object Stellung
object AktiveSeite: Seite

enum class Seite {
    weiss, schwarz
}

enum class Aktion {
    Zug, RemisAnbieten, aufgeben
}

class Zustand {
    val schach = Schach
    val stellung = Stellung
    val aktiveSeite = AktiveSeite
}

object AktuellerZustand: Zustand
typealias Zustandshistorie = List<Zustand>
typealias Aktionshistorie = List<Aktion>

enum class SpielerStatus {
    aktiv, inaktiv
}
class Spieler(val seite: Seite) {
    var status: SpielerStatus = SpielerStatus.inaktiv
}
sealed class Ergebnis

object Schachmatt: Ergebnis()

class Remis: Ergebnis() {
    val grund: RemisGrund
}

enum class RemisGrund {
    Patt, Einigung, Stellungswiederholung
}

class Partie {
    val aktuellerZustand = AktuellerZustand
    val zustandshistorie = Zustandshistorie
    val aktionshistorie = Aktionshistorie
    val spielerInWeiss = Spieler()
    val spielerInSchwarz = Spieler()
    val ergebnis = Ergebnis
}