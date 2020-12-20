package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move

interface ChessGameEvent : Event {
    fun movePiece(move: Move): PieceMovedOrNot

    fun getActivePlayer(): Player
    fun pieceOn(arrivalSquare: Square): PieceOrNoPiece
    fun moveOptions(): Set<Move>
    val id: ChessGameId
    val position: Position
    val white: Player
    val black: Player
    val numberOfNextMove: Int
    val movesWithoutCaptureOrPawnMove: Int
    val moves: List<PositionChanged>
    val status: Status
}

data class PiecesHaveBeenSetUp(val chessGame: ChessGame) : ChessGameEvent by chessGame

sealed class PieceMovedOrNot : ChessGameEvent {
    abstract val chessGame: ChessGame
}

class PieceMoved(
    val move: Move,
    override val chessGame: ChessGame
) : PieceMovedOrNot(), ChessGameEvent by chessGame

class PieceNotMoved(
    val reason: String,
    override val chessGame: ChessGame
) : PieceMovedOrNot(), ChessGameEvent by chessGame

sealed class MoveCalculatedOrNot : ChessGameEvent {
    abstract val chessGame: ChessGame
}

class MoveCalculated(
    val move: Move,
    override val chessGame: ChessGame
) : MoveCalculatedOrNot(), ChessGameEvent by chessGame

class NoMoveCalculated(
    val reason: String,
    override val chessGame: ChessGame
) : MoveCalculatedOrNot(), ChessGameEvent by chessGame