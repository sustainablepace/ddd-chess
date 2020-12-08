package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.CalculatedMove
import net.sustainablepace.chess.domain.ChessGame
import net.sustainablepace.chess.domain.Move
import net.sustainablepace.chess.domain.NoMove

sealed class Player

abstract class ComputerPlayer : Player() {
    open fun calculateMove(chessGame: ChessGame): CalculatedMove = NoMove
}

object StupidComputerPlayer : ComputerPlayer() {

    override fun calculateMove(chessGame: ChessGame): CalculatedMove = with(chessGame) {
        (position.getFirstOccupiedSquare(turn) to position.getFirstEmptySquare()).let { (departureSquare, arrivalSquare) ->
            if (departureSquare != null && arrivalSquare != null) {
                Move("$departureSquare-$arrivalSquare")
            } else
                NoMove
        }
    }
}

object HumanPlayer : Player()