package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.ChessGameEvent
import net.sustainablepace.chess.domain.event.PieceMoved
import net.sustainablepace.chess.domain.event.PieceNotMoved
import kotlin.random.Random

sealed class Engine {
    abstract fun bestMove(chessGame: ChessGameEvent): ValidMove?

    fun sophisticatedEvaluation(board: Board, engineSide: Side): Double {
        return setOf(White, Black).sumByDouble { side ->
            val factor = if (side == engineSide) 1 else -1
            factor * board.findPieces(side).sumByDouble { (square, piece) ->
                when (piece) {
                    is Pawn -> 10
                    is Knight -> 30
                    is Bishop -> 30
                    is Rook -> 50
                    is Queen -> 90
                    is King -> 900
                } * Weight(square, piece)
            }
        }
    }

    fun simpleEvaluation(board: Board, engineSide: Side): Int =
        setOf(White, Black).sumBy { side ->
            val factor = if (side == engineSide) 1 else -1
            factor * board.findPieces(side).sumBy { (_, piece) ->
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

object Minimax : Engine() {
    override fun bestMove(chessGame: ChessGameEvent): ValidMove? =
        bestCase(chessGame, chessGame.position.turn)?.first

    fun bestCase(chessGame: ChessGameEvent, engineSide: Side): Pair<ValidMove, Int>? {
        return chessGame.moveOptions().map { engineMove ->
            chessGame.movePiece(engineMove).let { opponentChessGame ->
                worstCase(opponentChessGame, engineMove, engineSide)
            }
        }.maxByOrNull { it.second }
    }

    fun worstCase(opponentChessGame: ChessGameEvent, engineMove: ValidMove, engineSide: Side): Pair<ValidMove, Int> {
        return when (opponentChessGame.status) {
            Checkmate -> engineMove to 10000000
            InProgress -> opponentChessGame.moveOptions().map { opponentMove ->
                opponentChessGame.movePiece(opponentMove).let {
                    engineMove to simpleEvaluation(it.position.board, engineSide)
                }
            }.minByOrNull { it.second }!!
            else -> engineMove to 0
        }
    }
}


object MinimaxWithDepth : Engine() {
    override fun bestMove(chessGame: ChessGameEvent): ValidMove? =
        bestCase(chessGame, chessGame.position.turn, 2)?.first

    private fun bestCase(chessGame: ChessGameEvent, engineSide: Side, depth: Int): Pair<ValidMove, Int>? {
        return when (chessGame.status) {
            InProgress ->
                if (chessGame.position.turn != engineSide) {
                    throw IllegalStateException("Mixed up with turns")
                } else chessGame.moveOptions().map { engineMove ->
                    when (val event = chessGame.movePiece(engineMove)) {
                        is PieceMoved -> event.let { opponentChessGame ->
                            worstCase(opponentChessGame, engineMove, engineSide, depth)
                        }
                        is PieceNotMoved -> throw IllegalStateException("Move $engineMove not possible")
                    }
                }.maxByOrNull { it.second }
            else -> null
        }
    }

    private fun worstCase(
        opponentChessGame: ChessGameEvent,
        engineMove: ValidMove,
        engineSide: Side,
        depth: Int
    ): Pair<ValidMove, Int> {
        if (opponentChessGame.position.turn == engineSide) {
            throw IllegalStateException("Mixed up with turns")
        }
        return when (opponentChessGame.status) {
            Checkmate -> engineMove to 10000000
            InProgress -> {
                val worstCase = opponentChessGame.moveOptions().map { opponentMove ->
                    opponentChessGame.movePiece(opponentMove).let {
                        opponentMove to simpleEvaluation(it.position.board, engineSide)
                    }
                }.minByOrNull { it.second }!!
                val r = if (depth == 1) {
                    engineMove to worstCase.second
                } else {
                    val bestCase: Pair<ValidMove, Int>? =
                        bestCase(opponentChessGame.movePiece(worstCase.first), engineSide, depth - 1)
                    return if (bestCase == null) {
                        engineMove to worstCase.second
                    } else {
                        engineMove to bestCase.second
                    }
                }
                //println("$r, $depth")
                r
            }
            else -> engineMove to 0
        }
    }
}

