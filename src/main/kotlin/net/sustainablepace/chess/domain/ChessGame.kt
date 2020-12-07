package net.sustainablepace.chess.domain

class ChessGame private constructor(
    val id: ChessGameId,
    val position: MutableMap<String, String>,
    var turn: String,
    val white: String,
    val black: String,
    var status: String
) {

    fun movePiece(move:Move): ChessGame {
        position.set(move.arrivalSquare, position.getValue(move.departureSquare))
        position.remove(move.departureSquare)
        if (!position.values.map { it[0] }.containsAll(listOf('b', 'w'))) {
            status = "checkmate"
        }
        if (turn == "white") {
            turn = "black"
        } else {
            turn = "white"
        }
        return this
    }

    constructor() : this(defaultPosition)
    private constructor(position: MutableMap<String, String>) : this(
        id = chessGameId(),
        position = position,
        turn = "white",
        white = "human",
        black = "computer",
        status = "in progress"
    )

    companion object {
        val defaultPosition = mapOf(
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