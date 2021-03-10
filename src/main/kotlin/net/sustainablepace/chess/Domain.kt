package net.sustainablepace.chess

typealias Schach = Boolean


enum class Seite {
    weiss, schwarz
}

enum class Aktion {
    Zug, RemisAnbieten, aufgeben
}

open class Zustand(
    val schach: Schach,
    val stellung: Stellung,
    val aktiveSeite: Seite
)

object AnfangsZustand: Zustand(false, Ausgangsstellung ,Seite.weiss)

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

class Remis(val grund: RemisGrund): Ergebnis()

enum class RemisGrund {
    Patt, Einigung, Stellungswiederholung
}

class Partie {
    val aktuellerZustand = AnfangsZustand
    val zustandshistorie = emptyList<Zustand>()
    val aktionshistorie = emptyList<Aktion>()
    val spielerInWeiss = Spieler(Seite.weiss)
    val spielerInSchwarz = Spieler(Seite.schwarz)
    var ergebnis: Ergebnis? = null
}

typealias Stellung =  Map<Char, List<Feld>>


class Brett {
    val stellung: Stellung = initAusgangstellung()

    companion object {
        fun initAusgangstellung(): Stellung = mapOf()
    }

}

enum Figur {
    Rook, Knight, Queen, Bishop, King, Pawn
}

class Feld(val spalte: Char, val zeile: Int) {
    var figur: Figur? = null

    fun isOnGrundlinie(): Boolean = false

}

