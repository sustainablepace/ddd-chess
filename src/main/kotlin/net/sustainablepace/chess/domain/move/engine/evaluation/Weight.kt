package net.sustainablepace.chess.domain.move.engine.evaluation

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*

object Weight {
    operator fun invoke(square: Square, piece: Piece): Double {
        val rankIndex = when(piece.side) {
            White -> square.rank-1
            Black -> 8-square.rank
        }
        val fileIndex = square.file - 'a'

        return when (piece) {
            is Pawn -> pawnWeights
            is Knight -> knightWeights
            is Rook -> rookWeights
            is Bishop -> bishopWeights
            is Queen -> queenWeights
            is King -> kingWeights
        }[rankIndex][fileIndex]
    }

    private val pawnWeights = arrayOf(
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5),
        doubleArrayOf(0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5),
        doubleArrayOf(0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0),
        doubleArrayOf(0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5),
        doubleArrayOf(1.0, 1.0, 2.0, 3.0, 3.0, 3.0, 2.0, 1.0, 1.0),
        doubleArrayOf(5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    )

    private val kingWeights = arrayOf(
        doubleArrayOf(2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0),
        doubleArrayOf(2.0, 2.0, 0.0, 0.0, 0.0, 0.0, 2.0, 2.0),
        doubleArrayOf(-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0),
        doubleArrayOf(-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0),
        doubleArrayOf(-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0),
        doubleArrayOf(-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0),
        doubleArrayOf(-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0),
        doubleArrayOf(-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0)
    )

    private val queenWeights = arrayOf(
        doubleArrayOf(-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0),
        doubleArrayOf(-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0),
        doubleArrayOf(-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0),
        doubleArrayOf(0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5),
        doubleArrayOf(-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5),
        doubleArrayOf(-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0),
        doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0),
        doubleArrayOf(-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0)
    )

    private val bishopWeights = arrayOf(
        doubleArrayOf(-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0),
        doubleArrayOf(-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0),
        doubleArrayOf(-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0),
        doubleArrayOf(-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0),
        doubleArrayOf(-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0),
        doubleArrayOf(-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0),
        doubleArrayOf(-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0),
        doubleArrayOf(-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0)
    )

    private val knightWeights = arrayOf(
        doubleArrayOf(-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0),
        doubleArrayOf(-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0),
        doubleArrayOf(-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0),
        doubleArrayOf(-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0),
        doubleArrayOf(-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0),
        doubleArrayOf(-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0),
        doubleArrayOf(-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0),
        doubleArrayOf(-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0)
    )

    private val rookWeights = arrayOf(
        doubleArrayOf(0.0, -0.5, 0.0, 0.5, 0.5, 0.0, -0.5, 0.0),
        doubleArrayOf(-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5),
        doubleArrayOf(-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5),
        doubleArrayOf(-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5),
        doubleArrayOf(-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5),
        doubleArrayOf(-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5),
        doubleArrayOf(0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5),
        doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    )
}

