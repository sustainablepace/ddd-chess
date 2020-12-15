package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.event.MoveCalculated
import net.sustainablepace.chess.domain.event.MoveCalculatedOrNot
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import kotlin.random.Random.Default.nextInt

sealed class Player

abstract class ComputerPlayer : Player() {
    abstract fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot
}

object StupidComputerPlayer : ComputerPlayer() {
    override fun calculateMove(chessGame: ChessGame): MoveCalculatedOrNot =
        chessGame.moveOptions().toList().let { moves ->
            when (moves.size) {
                0 -> NoMoveCalculated(
                    reason = "No move available. Game is in status ${chessGame.getStatus()}",
                    chessGame = chessGame
                )
                else -> MoveCalculated(
                    move = moves[nextInt(0, moves.size)],
                    chessGame = chessGame
                )
            }
        }
}

object HumanPlayer : Player()