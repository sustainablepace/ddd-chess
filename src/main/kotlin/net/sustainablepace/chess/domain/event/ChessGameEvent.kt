package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

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