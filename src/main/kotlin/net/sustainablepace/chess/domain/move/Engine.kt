package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.ChessGameEvent
import kotlin.random.Random

sealed class Engine {
    abstract fun bestMove(chessGame: ChessGameEvent): ValidMove?
}

object RandomMove : Engine() {
    override fun bestMove(chessGame: ChessGameEvent) =
        chessGame.moveOptions().let { moves ->
            moves.toList()[Random.nextInt(0, moves.size)]
        }
}

object AlwaysCaptures : Engine() {
    override fun bestMove(chessGame: ChessGameEvent): ValidMove? =
        chessGame.moveOptions().let { moves ->
        moves.maxByOrNull { move ->
            chessGame.movePiece(move).chessGame.let {
                setOf(White, Black).sumBy { side ->
                    val factor = if (side != it.position.turn) 1 else -1

                    factor * it.position.board.findPieces(side).sumBy { (_, piece) ->
                        when (piece) {
                            is Pawn -> 10
                            is Knight -> 30
                            is Bishop -> 30
                            is Rook -> 50
                            is Queen -> 90
                            is King -> 900
                        }
                    }
                }
            }
        }
    }
}