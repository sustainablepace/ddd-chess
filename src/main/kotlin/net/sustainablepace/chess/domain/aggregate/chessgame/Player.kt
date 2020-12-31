package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.event.MoveCalculated
import net.sustainablepace.chess.domain.event.MoveCalculatedOrNot
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import net.sustainablepace.chess.domain.move.*

sealed class Player

abstract class ComputerPlayer(val engine: Engine) : Player() {
    abstract fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot
}

object StupidComputerPlayer : ComputerPlayer(RandomMove) {
    override fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot =
        engine.bestMove(chessGame)?.let {
            MoveCalculated(
                move = it,
                chessGame = chessGame
            )
        } ?: NoMoveCalculated(
            reason = "No move available. Game is in status ${chessGame.status}",
            chessGame = chessGame
        )
}

object AggressiveStupidComputerPlayer : ComputerPlayer(AlwaysCaptures) {
    override fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot =
        engine.bestMove(chessGame)?.let {
            MoveCalculated(
                move = it,
                chessGame = chessGame
            )
        } ?: NoMoveCalculated(
            reason = "No move available. Game is in status ${chessGame.status}",
            chessGame = chessGame
        )
}

object MinimaxComputerPlayer : ComputerPlayer(Minimax) {
    override fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot =
        engine.bestMove(chessGame)?.let {
            MoveCalculated(
                move = it,
                chessGame = chessGame
            )
        } ?: NoMoveCalculated(
            reason = "No move available. Game is in status ${chessGame.status}",
            chessGame = chessGame
        )
}

object MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer : ComputerPlayer(MinimaxWithDepthAndSophisticatedEvaluation) {
    override fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot =
        engine.bestMove(chessGame)?.let {
            MoveCalculated(
                move = it,
                chessGame = chessGame
            )
        } ?: NoMoveCalculated(
            reason = "No move available. Game is in status ${chessGame.status}",
            chessGame = chessGame
        )
}

object HumanPlayer : Player()