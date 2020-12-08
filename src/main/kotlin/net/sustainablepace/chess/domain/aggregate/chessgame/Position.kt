package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.ValidMove
import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Colour
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White

class Position(val squaresWithPieces: Map<Square, Piece>) {
    fun movePiece(move: ValidMove): Position =
        mutableMapOf<Square, Piece>().run {
            putAll(squaresWithPieces)
            this[move.arrivalSquare] = getValue(move.departureSquare)
            remove(move.departureSquare)
            this
        }.toMap().let {
            Position(it)
        }

    fun get(square: Square) = squaresWithPieces.get(square)

    val allSquares = ('a'..'h').flatMap { column ->
        ('1'..'8').map { column + "" + it }
    }

    val occupiedSquares: Set<Square>
        get() = squaresWithPieces.keys

    fun containsWhiteAndBlackPieces(): Boolean =
        squaresWithPieces.values.map { it.colour }.containsAll(listOf(White, Black))

    fun getFirstOccupiedSquare(colour: Colour) = squaresWithPieces.filter {
        it.value.colour == colour
    }.keys.firstOrNull()

    fun getFirstEmptySquare() = (allSquares - occupiedSquares).firstOrNull()

    companion object {
        val default = Position(mapOf<Square, Piece>(
            "a1" to WhiteRook(),
            "b1" to WhiteKnight(),
            "c1" to WhiteBishop(),
            "d1" to WhiteQueen(),
            "e1" to WhiteKing(),
            "f1" to WhiteBishop(),
            "g1" to WhiteKnight(),
            "h1" to WhiteRook(),
            "a2" to WhitePawn(),
            "b2" to WhitePawn(),
            "c2" to WhitePawn(),
            "d2" to WhitePawn(),
            "e2" to WhitePawn(),
            "f2" to WhitePawn(),
            "g2" to WhitePawn(),
            "h2" to WhitePawn(),
            "a8" to BlackRook(),
            "b8" to BlackKnight(),
            "c8" to BlackBishop(),
            "d8" to BlackQueen(),
            "e8" to BlackKing(),
            "f8" to BlackBishop(),
            "g8" to BlackKnight(),
            "h8" to BlackRook(),
            "a7" to BlackPawn(),
            "b7" to BlackPawn(),
            "c7" to BlackPawn(),
            "d7" to BlackPawn(),
            "e7" to BlackPawn(),
            "f7" to BlackPawn(),
            "g7" to BlackPawn(),
            "h7" to BlackPawn()
        ))
    }
}

