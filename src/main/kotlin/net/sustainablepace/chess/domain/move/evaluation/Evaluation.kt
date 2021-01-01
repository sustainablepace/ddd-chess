package net.sustainablepace.chess.domain.move.evaluation

import net.sustainablepace.chess.domain.aggregate.chessgame.*

fun Piece.value() = when (this) {
    is Pawn -> 10.0
    is Knight -> 30.0
    is Bishop -> 30.0
    is Rook -> 50.0
    is Queen -> 90.0
    is King -> 900.0
}

abstract class Evaluation {
    abstract fun evaluate(board: Board, engineSide: Side): Double


}

object SimpleEvaluation: Evaluation() {
    override fun evaluate(board: Board, engineSide: Side): Double {
        return setOf(White, Black).sumByDouble { side ->
            when (side) {
                White -> board.whitePieces
                Black -> board.blackPieces
            }.sumByDouble { (_, piece) ->
                piece.value()
            } * if (side == engineSide) 1 else -1
        }
    }
}

object WeighedEvaluation: Evaluation() {
    override fun evaluate(board: Board, engineSide: Side): Double {
        return setOf(White, Black).sumByDouble { side ->
            when (side) {
                White -> board.whitePieces
                Black -> board.blackPieces
            }.sumByDouble { (square, piece) ->
                piece.value() + Weight(square, piece)
            } * if (side == engineSide) 1 else -1
        }
    }
}