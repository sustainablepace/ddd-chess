package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

interface Event

interface ChessGameEvent : Event {
    fun getActivePlayer(): Player
    fun pieceOn(arrivalSquare: Square): PieceOrNoPiece
    fun moveOptions(): Set<ValidMove>
    fun movePiece(move: ValidMove): PieceMovedOrNot
    val id: ChessGameId
    val position: Position
    val turn: Side
    val white: Player
    val black: Player
    val status: Status
    val numberOfNextMove: Int
    val fiftyMoveRule: Int
}

data class PiecesHaveBeenSetUp(val chessGame: ChessGame): ChessGameEvent by chessGame

sealed class PieceMovedOrNot: ChessGameEvent {
    abstract val chessGame: ChessGame
}
class PieceMoved(val move: ValidMove, override val chessGame: ChessGame): PieceMovedOrNot(), ChessGameEvent by chessGame
class PieceNotMoved(val reason: String, override val chessGame: ChessGame): PieceMovedOrNot(), ChessGameEvent by chessGame

sealed class MoveCalculatedOrNot: Event
class MoveCalculated(val move: ValidMove, val chessGame: ChessGame): MoveCalculatedOrNot()
class NoMoveCalculated(val reason: String): MoveCalculatedOrNot()


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
