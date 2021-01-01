package net.sustainablepace.chess.domain.move.engine

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.evaluation.SimpleEvaluation

object AlwaysCaptures : Engine() {
    override fun bestMove(chessGame: ChessGame): ValidMove? =
        chessGame.moveOptions.maxByOrNull { move ->
            chessGame.movePiece(move).let {
                SimpleEvaluation.evaluate(it.position.board, chessGame.position.turn).toInt()
            }
        }
}