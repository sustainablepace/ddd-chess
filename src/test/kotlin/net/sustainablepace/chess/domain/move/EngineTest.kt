package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.MoveCalculated
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class EngineTest {
    @Test
    fun `stupid vs aggressive stupid`() {
        (1..10)
            .play(AggressiveStupidComputerPlayer, StupidComputerPlayer)
            .let { (player1Points, player2Points) ->
                println("Aggressive stupid: $player1Points, Stupid: $player2Points")
            }
    }

    @Test
    fun `minimax vs aggressive stupid`() {
        (1..3)
            .play(AggressiveStupidComputerPlayer, MinimaxComputerPlayer)
            .let { (player1Points, player2Points) ->
                println("Aggressive stupid: $player1Points, Minimax: $player2Points")
            }
    }

    @Test
    @Disabled
    fun `minimax vs minimax with depth`() {
        (1..1)
            .play(MinimaxWithDepthComputerPlayer, MinimaxComputerPlayer)
            .let { (player1Points, player2Points) ->
                println("Minimax with depth: $player1Points, Minimax: $player2Points")
            }
    }

    @Test
    fun `mate in two`() {
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    g1 to WhiteKing,
                    a2 to WhitePawn,
                    c2 to WhiteQueen,
                    f2 to WhitePawn,
                    g2 to WhitePawn,
                    h2 to WhitePawn,
                    a3 to WhiteBishop,
                    f3 to WhiteKnight,
                    e5 to WhitePawn,
                    a5 to BlackBishop,
                    c6 to BlackKnight,
                    a7 to BlackPawn,
                    b7 to BlackPawn,
                    c7 to BlackPawn,
                    d7 to BlackQueen,
                    f7 to BlackKing,
                    g7 to BlackPawn,
                    h7 to BlackPawn,
                    a8 to BlackRook,
                    e8 to BlackRook
                )
            )
        )
        MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame).apply {
            when (this) {
                is MoveCalculated -> assertThat(move).isEqualTo(Move(f3, g5))
                is NoMoveCalculated -> fail("No move calculated.")
            }
        }.run {
            movePiece(Move(f3, g5)).movePiece(Move(f7, g8)).let {
                MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(it)
            }
        }.apply {
            when (this) {
                is MoveCalculated -> assertThat(move).isEqualTo(Move(c2, h7))
                is NoMoveCalculated -> fail("No move calculated.")
            }
            assertThat(movePiece(move).status).isEqualTo(Checkmate)
        }
    }

    @Test
    fun `another mate in two`() {
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    c1 to WhiteRook,
                    g1 to WhiteKing,
                    a2 to WhitePawn,
                    b2 to WhitePawn,
                    f2 to WhitePawn,
                    g3 to WhitePawn,
                    h2 to WhitePawn,
                    g2 to WhiteBishop,
                    f6 to WhiteKnight,
                    d7 to WhiteQueen,
                    a8 to BlackRook,
                    b8 to BlackKing,
                    h8 to BlackRook,
                    a7 to BlackPawn,
                    b7 to BlackPawn,
                    d6 to BlackPawn,
                    b6 to BlackQueen,
                    f7 to BlackPawn,
                    h7 to BlackPawn
                )
            )
        )
        MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame.chessGame).run {
            when (this) {
                is MoveCalculated -> assertThat(move).isIn(Move(d7, c8), Move(d7, e8))
                is NoMoveCalculated -> fail("No move calculated.")
            }
            this
        }.run {


            movePiece(move).movePiece(Move(h8, move.arrivalSquare)).let {
                MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(it)
            }
        }.apply {
            when (this) {
                is MoveCalculated -> assertThat(move).isEqualTo(Move(f6, d7))
                is NoMoveCalculated -> fail("No move calculated.")
            }
            assertThat(movePiece(move).chessGame.status).isEqualTo(Checkmate)
        }
    }

    @Test
    fun `first move`() {
        MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame()).run {
            when (this) {
                is MoveCalculated -> {
                    val score = Minimax.sophisticatedEvaluation(movePiece(move).position.board, White)
                    assertThat(score).isGreaterThan(0.0)
                }
                is NoMoveCalculated -> fail("Must find a first move.")
            }
        }
    }


    @Test
    fun `second move`() {
        val chessGame = chessGame().movePiece(Move(g1, f3)).movePiece(Move(d7, d6))
        MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame).run {
            when (this) {
                is MoveCalculated -> {
                    val score = Minimax.sophisticatedEvaluation(position.board, White)
                    assertThat(score).isGreaterThan(0.0)
                }
                is NoMoveCalculated -> fail("Must find a first move.")
            }

        }

    }

    @Test
    fun `prevents mate in one`() {
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    a1 to WhiteRook,
                    b1 to WhiteKnight,
                    c1 to WhiteBishop,
                    d1 to WhiteQueen,
                    e1 to WhiteKing,
                    f1 to WhiteBishop,
                    f3 to WhiteKnight,
                    h1 to WhiteRook,
                    a2 to WhitePawn,
                    c2 to WhitePawn,
                    d2 to WhitePawn,
                    e2 to WhitePawn,
                    f2 to WhitePawn,
                    g2 to WhitePawn,
                    h4 to WhitePawn,
                    a8 to BlackRook,
                    b8 to BlackKnight,
                    c8 to BlackBishop,
                    b6 to BlackQueen,
                    e8 to BlackKing,
                    c5 to BlackBishop,
                    g8 to BlackKnight,
                    h8 to BlackRook,
                    a7 to BlackPawn,
                    b7 to BlackPawn,
                    c6 to BlackPawn,
                    d7 to BlackPawn,
                    e6 to BlackPawn,
                    f7 to BlackPawn,
                    g7 to BlackPawn,
                    h7 to BlackPawn
                )
            )
        )
        MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame).run {
            when (this) {
                is MoveCalculated -> {
                    chessGame.movePiece(move).let {
                        assertThat(it.numberOfNextMove).isEqualTo(2)
                        assertThat(it.moveOptions()).doesNotContain(Move(c5, f2))
                    }
                }
                is NoMoveCalculated -> fail("Must find a defensive move.")
            }
        }
    }

    private fun IntRange.play(player1: ComputerPlayer, player2: ComputerPlayer): Pair<Double, Double> =
        map { numberOfGame ->
            if (numberOfGame % 2 == 0) {
                chessGame(player1, player2)
            } else {
                chessGame(player2, player1)
            }
        }.map {
            var chessGame = it
            while (chessGame.status == InProgress) {
                val player = chessGame.activePlayer as ComputerPlayer
                when (val move = player.calculateMove(chessGame)) {
                    is MoveCalculated -> chessGame = chessGame.movePiece(move.move)
                    is NoMoveCalculated -> fail("If the game is in progress, there should be at least one valid move.")
                }
            }
            chessGame
        }.map { chessGame ->
            when (chessGame.status) {
                Checkmate -> when (chessGame.position.turn) {
                    White -> if (chessGame.white is StupidComputerPlayer) 0.0 to 1.0 else 1.0 to 0.0
                    Black -> if (chessGame.black is StupidComputerPlayer) 0.0 to 1.0 else 1.0 to 0.0
                }
                InProgress -> fail("Game should be over!")
                else -> 0.5 to 0.5
            }
        }.let {
            val player1Points = it.sumByDouble { it.first }
            val player2Points = it.sumByDouble { it.second }
            player1Points to player2Points
        }
}