package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

interface PositionEvent : Event {
    fun movePiece(move: ValidMove): PieceMovedOnBoardOrNot

    fun isInCheck(): Boolean
    fun isSquareThreatenedBy(threatenedSquare: Square, side: Side): Boolean
    fun pieceOn(square: Square): PieceOrNoPiece
    fun moveOptions(): Set<ValidMove>
    fun moveOptionsForSquare(square: Square): Set<ValidMove>

    val board: Board
    val enPassantSquare: EnPassantSquare
    val whiteCastlingOptions: CastlingOptions
    val blackCastlingOptions: CastlingOptions
    val turn: Side
}

sealed class PositionChanged: PositionEvent{
    abstract val position: Position
}

sealed class PieceMovedOnBoardOrNot : PositionChanged() {
    abstract val move: ValidMove
    abstract val pieceCapturedOrPawnMoved: Boolean
}

class PieceMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: ValidMove
) : PieceMovedOnBoardOrNot(), PositionEvent by position

class PieceNotMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: ValidMove
) : PieceMovedOnBoardOrNot(), PositionEvent by position

class PositionSetUp(
    override val position: Position
) : PositionChanged(), PositionEvent by position