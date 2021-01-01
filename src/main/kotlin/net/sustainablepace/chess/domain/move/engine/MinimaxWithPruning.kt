package net.sustainablepace.chess.domain.move.engine

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.Checkmate
import net.sustainablepace.chess.domain.aggregate.chessgame.InProgress
import net.sustainablepace.chess.domain.aggregate.chessgame.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.Side
import net.sustainablepace.chess.domain.event.PieceMoved
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.evaluation.WeighedEvaluation
import net.sustainablepace.chess.domain.move.evaluation.value
import kotlin.random.Random

data class MinimaxData(val engineMove: ValidMove?, val score: Double, val depth: Int)
object MinimaxWithDepthAndSophisticatedEvaluation : Engine() {
    override fun bestMove(chessGame: ChessGame): ValidMove? { // TODO: return all moves, pick random one in service?
        val moves = minimax(chessGame, 4, chessGame.position.turn, -Double.MAX_VALUE, Double.MAX_VALUE)

        return if (moves.isNotEmpty()) {
            moves.sortedBy { it.depth }[Random.nextInt(0, moves.size)].engineMove
        } else null
    }

    private fun minimax(
        chessGame: ChessGame,
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
                    val moves = chessGame.moveOptions.sortedByDescending { move ->
                        val piece = chessGame.pieceOn(move.departureSquare)
                        val capturedPiece = chessGame.pieceOn(move.arrivalSquare)
                        when(piece) {
                            is Piece -> piece.value() + when(capturedPiece) {
                                is Piece -> 1000.0
                                else -> 0.0
                            }
                            else -> 0.0
                        }
                    }
                    val minimaxDataList: MutableList<MinimaxData> = mutableListOf()

                    for (engineMove in moves) {
                        val event = chessGame.movePiece(engineMove)
                        if (event is PieceMoved) {
                            val minimaxData = if (depth == 1) {
                                WeighedEvaluation.evaluate(
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
                    val moves = chessGame.moveOptions.sortedByDescending { move ->
                        val piece = chessGame.pieceOn(move.departureSquare)
                        val capturedPiece = chessGame.pieceOn(move.arrivalSquare)
                        when(piece) {
                            is Piece -> piece.value() + when(capturedPiece) {
                                is Piece -> 1000.0
                                else -> 0.0
                            }
                            else -> 0.0
                        }
                    }
                    val minimaxDataList: MutableList<MinimaxData> = mutableListOf()

                    for (engineMove in moves) {
                        val event = chessGame.movePiece(engineMove)
                        if (event is PieceMoved) {
                            val minimaxData = if (depth == 1) {
                                WeighedEvaluation.evaluate(
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