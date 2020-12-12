package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.CalculatedMove
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.NoMove
import kotlin.random.Random.Default.nextInt

sealed class Player

abstract class ComputerPlayer : Player() {
    open fun calculateMove(chessGame: ChessGame): CalculatedMove = NoMove
}

object StupidComputerPlayer : ComputerPlayer() {
    override fun calculateMove(chessGame: ChessGame): CalculatedMove = with(chessGame) {
        moveOptions().toList().let { moves ->
            if(moves.isNotEmpty()) {
                moves[nextInt(0, moves.size)].run {
                    Move("$departureSquare-$arrivalSquare")
                }
            } else NoMove
        }
    }
}

object HumanPlayer : Player()