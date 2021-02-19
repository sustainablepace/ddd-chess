package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.Color.*
import net.sustainablepace.chess.domain.File.*
import net.sustainablepace.chess.domain.Rank.*

sealed class Player (val color: Color)

object Computer: Player(BLACK)
object Human: Player(WHITE)

enum class Color {
    WHITE, BLACK
}

sealed class Piece(val color: Color)
class King(color: Color): Piece(color)
class Queen(color: Color): Piece(color)
class Rook(color: Color): Piece(color)
class Bishop(color: Color): Piece(color)
class Knight(color: Color): Piece(color)
class Pawn(color: Color): Piece(color)

enum class Rank {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT
}

enum class File {
    A, B, C, D, E, F, G, H
}

enum class GameState {
    IN_PROGRESS, DRAW, WHITE_WINS, BLACK_WINS
}

typealias Square = Pair<File, Rank>

typealias Position = Map<Square, Piece>

typealias Move = Pair<Square, Square>

val initialPosition: Position =
    mapOf(
        Square(A, ONE) to Rook(WHITE),
        Square(B, ONE) to Knight(WHITE),
        Square(C, ONE) to Bishop(WHITE),
        Square(D, ONE) to Queen(WHITE),
        Square(E, ONE) to King(WHITE),
        Square(F, ONE) to Bishop(WHITE),
        Square(G, ONE) to Knight(WHITE),
        Square(H, ONE) to Rook(WHITE),

        Square(A, TWO) to Pawn(WHITE),
        Square(B, TWO) to Pawn(WHITE),
        Square(C, TWO) to Pawn(WHITE),
        Square(D, TWO) to Pawn(WHITE),
        Square(E, TWO) to Pawn(WHITE),
        Square(F, TWO) to Pawn(WHITE),
        Square(G, TWO) to Pawn(WHITE),
        Square(H, TWO) to Pawn(WHITE),

        Square(A, SEVEN) to Pawn(BLACK),
        Square(B, SEVEN) to Pawn(BLACK),
        Square(C, SEVEN) to Pawn(BLACK),
        Square(D, SEVEN) to Pawn(BLACK),
        Square(E, SEVEN) to Pawn(BLACK),
        Square(F, SEVEN) to Pawn(BLACK),
        Square(G, SEVEN) to Pawn(BLACK),
        Square(H, SEVEN) to Pawn(BLACK),

        Square(A, EIGHT) to Rook(BLACK),
        Square(B, EIGHT) to Knight(BLACK),
        Square(C, EIGHT) to Bishop(BLACK),
        Square(D, EIGHT) to Queen(BLACK),
        Square(E, EIGHT) to King(BLACK),
        Square(F, EIGHT) to Bishop(BLACK),
        Square(G, EIGHT) to Knight(BLACK),
        Square(H, EIGHT) to Rook(BLACK),
    )

class Game(
        val id: ChessGameId, // todo: maybe change to uuid later
        var positions: List<Position> = listOf(initialPosition),
        var state: GameState = GameState.IN_PROGRESS,
        var activeColor: Color = WHITE,
) {
    fun applyMove(move: Move) {
        val piece = currentPosition.get(move.first)!!
        val newPosition: Position = (currentPosition - move.first) + (move.second to piece)
        positions += newPosition
    }


    val currentPosition: Position
        get() = positions.last()
}

class ChessGame private constructor(
    val id: ChessGameId,
    val position: MutableMap<String, String>,
    var turn: String,
    val white: String,
    val black: String,
    var status: String
) {

    constructor() : this(defaultPosition())
    private constructor(position: MutableMap<String, String>) : this(
        id = chessGameId(),
        position = position,
        turn = "white",
        white = "human",
        black = "computer",
        status = "in progress"
    )

    companion object {
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
