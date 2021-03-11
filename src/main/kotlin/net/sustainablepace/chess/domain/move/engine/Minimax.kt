package net.sustainablepace.chess.domain.move.engine

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.Checkmate
import net.sustainablepace.chess.domain.aggregate.chessgame.InProgress
import net.sustainablepace.chess.domain.aggregate.chessgame.Side
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.engine.evaluation.SimpleEvaluation

object Minimax : Engine() {
    override fun bestMove(chessGame: ChessGame): ValidMove? =
        bestCase(chessGame, chessGame.position.turn)?.first

    private fun bestCase(chessGame: ChessGame, engineSide: Side): Pair<ValidMove, Int>? {
        return chessGame.moveOptions.map { engineMove ->
            worstCase(chessGame.movePiece(engineMove), engineMove, engineSide)
        }.maxByOrNull { it.second }
    }

    private fun worstCase(opponentChessGame: ChessGame, engineMove: ValidMove, engineSide: Side): Pair<ValidMove, Int> {
        return when (opponentChessGame.status) {
            Checkmate -> engineMove to 10000000
            InProgress -> opponentChessGame.moveOptions.map { opponentMove ->
                opponentChessGame.movePiece(opponentMove).let {
                    engineMove to SimpleEvaluation.evaluate(it.position.board, engineSide).toInt()
                }
            }.minByOrNull { it.second }!!
            else -> engineMove to 0
        }
    }
}