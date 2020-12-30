package net.sustainablepace.chess.domain.event

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.ValidMove


sealed class MoveCalculatedOrNot : ChessGame, Event {
    abstract val chessGame: ChessGame
}

class MoveCalculated(
    val move: ValidMove,
    override val chessGame: ChessGame
) : MoveCalculatedOrNot(), ChessGame by chessGame

class NoMoveCalculated(
    val reason: String,
    override val chessGame: ChessGame
) : MoveCalculatedOrNot(), ChessGame by chessGame