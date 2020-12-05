package net.sustainablepace.chess.domain

typealias ChessGameId = String
typealias Position = MutableMap<String, String>

class ChessGame private constructor (val id: ChessGameId, val position: Position) {

    constructor() : this(defaultPosition())
    private constructor(position: Position): this(createChessGameId(), position)

    companion object {
        private fun createChessGameId(): ChessGameId {
            val charPool = ('a'..'z').toList() + ('1'..'9').toList()
            return (1..7)
                .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
        }
        private fun defaultPosition() = mapOf(
            "a1" to "wR",
            "b1" to "wN",
            "c1" to "wB",
            "d1" to "wQ",
            "e1" to "wK",
            "f1" to "wB",
            "g1" to "wN",
            "h1" to "wR",
            "a2" to "wP",
            "b2" to "wP",
            "c2" to "wP",
            "d2" to "wP",
            "e2" to "wP",
            "f2" to "wP",
            "g2" to "wP",
            "h2" to "wP",
            "a8" to "bR",
            "b8" to "bN",
            "c8" to "bB",
            "d8" to "bQ",
            "e8" to "bK",
            "f8" to "bB",
            "g8" to "bN",
            "h8" to "bR",
            "a7" to "bP",
            "b7" to "bP",
            "c7" to "bP",
            "d7" to "bP",
            "e7" to "bP",
            "f7" to "bP",
            "g7" to "bP",
            "h7" to "bP"
        ).toMutableMap()
    }
}