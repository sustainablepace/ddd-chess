package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move

interface PositionEvent : Event {
    fun movePiece(move: Move): PieceMovedOnBoardOrNot

    fun isInCheck(): Boolean
    fun isSquareThreatenedBy(threatenedSquare: Square, side: Side): Boolean
    fun pieceOn(square: Square): PieceOrNoPiece
    fun moveOptions(): Set<Move>
    fun moveOptionsIgnoringCheck(square: Square, ignoreKingMoves: Boolean = false): Set<Move>

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
    abstract val move: Move
    abstract val pieceCapturedOrPawnMoved: Boolean
}

class PieceMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: Move
) : PieceMovedOnBoardOrNot(), PositionEvent by position

class PieceNotMovedOnBoard(
    override val position: Position,
    override val pieceCapturedOrPawnMoved: Boolean,
    override val move: Move
) : PieceMovedOnBoardOrNot(), PositionEvent by position

class PositionSetUp(
    override val position: Position
) : PositionChanged(), PositionEvent by position