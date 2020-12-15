package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move

interface PositionEvent: Event {
    fun movePiece(move: Move): PositionChangedOrNot

    fun isInCheck(side: Side): Boolean
    fun pieceOn(square: Square): PieceOrNoPiece
    fun moveOptions(side: Side): Set<Move>
    fun moveOptionsIgnoringCheck(square: Square): Set<Move>
    val board: Board
    val enPassantSquare: EnPassantSquare
    val whiteCastlingOptions: CastlingOptions
    val blackCastlingOptions: CastlingOptions
}

sealed class PositionChangedOrNot : PositionEvent {
    abstract val position: Position
    abstract val pieceCapturedOrPawnMoved: Boolean
}

class PositionChanged(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean
) : PositionChangedOrNot(), PositionEvent by position

class PositionNotChanged(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean
) : PositionChangedOrNot(), PositionEvent by position