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
            val s = factor * board.findPieces(side).sumByDouble { (square, piece) ->
                when (piece) {
                    is Pawn -> 10
                    is Knight -> 30
                    is Bishop -> 30
                    is Rook -> 50
                    is Queen -> 90
                    is King -> 900
                } + Weight(square, piece)
            }
            s
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
                r
            }
            else -> engineMove to 0
        }
    }
}

data class MinimaxData(val engineMove: ValidMove?, val score: Double, val depth: Int)

object MinimaxWithDepthAndSophisticatedEvaluation : Engine() {
    override fun bestMove(chessGame: ChessGameEvent): ValidMove? {
        val moves = minimax(chessGame, 4, chessGame.position.turn, -Double.MAX_VALUE, Double.MAX_VALUE)

        return if (moves.size > 0) {
            moves.sortedBy { it.depth }[Random.nextInt(0, moves.size)].engineMove
        } else null
    }

    private fun minimax(
        chessGame: ChessGameEvent,
        depth: Int,
        maximizingSide: Side,
        alpha: Double,
        beta: Double
    ): List<MinimaxData> {
        var maxEval = -Double.MAX_VALUE
        var minEval = Double.MAX_VALUE
        var a = alpha
        var b = beta
        return if (chessGame.position.turn == maximizingSide) {
            when (chessGame.status) {
                Checkmate -> listOf(
                    MinimaxData(
                        engineMove = null,
                        score = -Double.MAX_VALUE,
                        depth = depth
                    )
                )
                InProgress -> {
                    val moves = chessGame.moveOptions()
                    val minimaxDataList: MutableList<MinimaxData> = mutableListOf()

                    for (engineMove in moves) {
                        val event = chessGame.movePiece(engineMove)
                        if (event is PieceMoved) {
                            val minimaxData = if (depth == 1) {
                                sophisticatedEvaluation(
                                    event.position.board,
                                    maximizingSide
                                ).let { eval ->
                                    listOf(
                                        MinimaxData(
                                            engineMove,
                                            eval,
                                            depth
                                        )
                                    )
                                }
                            } else {
                                minimax(event.chessGame, depth - 1, maximizingSide, a, b)
                            }
                            val eval = minimaxData.map { it.score }.firstOrNull() ?: -Double.MAX_VALUE
                            maxEval = if (eval > maxEval) eval else maxEval
                            a = if (eval > a) eval else a

                            minimaxDataList.addAll(minimaxData.map { it.copy(engineMove = engineMove) }.distinct())

                            if (b < a) {
                                break
                            }
                        }
                    }
                    minimaxDataList.filter { it.score == maxEval }
                }
                else -> listOf(MinimaxData(null, 0.0, depth))
            }
        } else {
            when (chessGame.status) {
                Checkmate -> listOf(
                    MinimaxData(
                        engineMove = null,
                        score = Double.MAX_VALUE,
                        depth = depth
                    )
                )
                InProgress -> {
                    val moves = chessGame.moveOptions()
                    val minimaxDataList: MutableList<MinimaxData> = mutableListOf()

                    for (engineMove in moves) {
                        val event = chessGame.movePiece(engineMove)
                        if (event is PieceMoved) {
                            val minimaxData = if (depth == 1) {
                                sophisticatedEvaluation(
                                    event.position.board,
                                    maximizingSide
                                ).let { eval ->
                                    listOf(
                                        MinimaxData(
                                            engineMove,
                                            eval,
                                            depth
                                        )
                                    )
                                }
                            } else {
                                minimax(event.chessGame, depth - 1, maximizingSide, a, b)
                            }
                            val eval = minimaxData.map { it.score }.firstOrNull() ?: Double.MAX_VALUE

                                minEval = if (eval < minEval) eval else minEval
                                b = if (eval < b) eval else b

                            minimaxDataList.addAll(minimaxData.map { it.copy(engineMove = engineMove) })

                            if (b < a) {
                                break
                            }
                        }
                    }
                    minimaxDataList.filter { it.score == minEval }.distinct()
                }
                else -> listOf(MinimaxData(null, 0.0, depth))
            }
        }

    }


}

