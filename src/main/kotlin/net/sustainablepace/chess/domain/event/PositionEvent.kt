package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

interface PositionEvent: Event {
    fun isInCheck(side: Side): Boolean
    fun pieceOn(square: Square): PieceOrNoPiece
    fun movePiece(move: ValidMove): PositionUpdatedOrNot
    fun moveOptions(side: Side): Set<ValidMove>
    fun moveOptionsIgnoringCheck(square: Square): Set<ValidMove>
    val board: Board
    val enPassantSquare: EnPassantSquare
    val whiteCastlingOptions: CastlingOptions
    val blackCastlingOptions: CastlingOptions
}

sealed class PositionUpdatedOrNot : PositionEvent {
    abstract val position: Position
    abstract val pieceCapturedOrPawnMoved: Boolean
}

class PositionUpdated(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean
) : PositionUpdatedOrNot(), PositionEvent by position

class PositionNotUpdated(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean
) : PositionUpdatedOrNot(), PositionEvent by position