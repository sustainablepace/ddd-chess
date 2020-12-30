package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.ValidMove

sealed class PieceMovedOrNot : ChessGame, Event {
    abstract val chessGame: ChessGame
}

class PieceMoved(
    val move: ValidMove,
    override val chessGame: ChessGame
) : PieceMovedOrNot(), ChessGame by chessGame

class PieceNotMoved(
    val reason: String,
    override val chessGame: ChessGame
) : PieceMovedOrNot(), ChessGame by chessGame