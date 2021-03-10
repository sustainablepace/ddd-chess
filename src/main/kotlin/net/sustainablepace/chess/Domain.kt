package net.sustainablepace.chess

typealias Schach = Boolean
object Stellung
object AktiveSeite
class Zustand {
    val schach = Schach
    val stellung = Stellung
    val aktiveSeite = AktiveSeite
}
object AktuellerZustand: Zustand
typealias Zustandshistorie = List<Zustand>
class Spieler {

}
object Ergebnis

class Partie {
    val aktuellerZustand = AktuellerZustand
    val zustandshistorie = Zustandshistorie
    val spielerInWeiss = Spieler()
    val spielerInSchwarz = Spieler()
    val ergebnis = Ergebnis
}