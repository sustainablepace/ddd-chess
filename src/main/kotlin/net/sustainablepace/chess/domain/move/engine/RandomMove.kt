package net.sustainablepace.chess.domain.move.engine

import net.sustainablepace.chess.domain.aggregate.ChessGame
import kotlin.random.Random

object RandomMove : Engine() {
    override fun bestMove(chessGame: ChessGame) =
        chessGame.moveOptions.let { moves ->
            moves.toList()[Random.nextInt(0, moves.size)]
        }
}