package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.MoveCalculated
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class EngineTest {

    @Test
    fun `stupid vs aggressive stupid`() {
        var stupidPoints = 0f
        var aggressiveStupidPoints = 0f
        (1..10).map { numberOfGame ->
            var chessGame = if(numberOfGame % 2 == 0) {
                ChessGame(AggressiveStupidComputerPlayer, StupidComputerPlayer)
            } else {
                ChessGame(StupidComputerPlayer, AggressiveStupidComputerPlayer)
            }.chessGame
            while (chessGame.status == InProgress) {
                val player = chessGame.getActivePlayer() as ComputerPlayer
                when(val move = player.calculateMove(chessGame)) {
                    is MoveCalculated -> chessGame = chessGame.movePiece(move.move).chessGame
                    is NoMoveCalculated -> throw IllegalStateException("If the game is in progress, there should be at least one valid move.")
                }
            }
            when (chessGame.status) {
                Checkmate -> when( chessGame.position.turn ) {
                    White -> if(chessGame.white is AggressiveStupidComputerPlayer) 0f to 1f else 1f to 0f
                    Black -> if(chessGame.black is AggressiveStupidComputerPlayer) 0f to 1f else 1f to 0f
                }
                InProgress -> throw IllegalStateException("Game should be over!")
                else -> 0.5f to 0.5f
            }.let { (aggressiveStupid, stupid) ->
                stupidPoints += stupid
                aggressiveStupidPoints += aggressiveStupid
            }
            println("Aggressive stupid: $aggressiveStupidPoints, Stupid: $stupidPoints")
        }

    }

    @Test
    fun `minimax vs aggressive stupid`() {
        var stupidPoints = 0f
        var aggressiveStupidPoints = 0f
        (1..3).map { numberOfGame ->
            var chessGame = if(numberOfGame % 2 == 0) {
                ChessGame(AggressiveStupidComputerPlayer, MinimaxComputerPlayer)
            } else {
                ChessGame(MinimaxComputerPlayer, AggressiveStupidComputerPlayer)
            }.chessGame
            while (chessGame.status == InProgress) {
                val player = chessGame.getActivePlayer() as ComputerPlayer
                when(val move = player.calculateMove(chessGame)) {
                    is MoveCalculated -> chessGame = chessGame.movePiece(move.move).chessGame
                    is NoMoveCalculated -> throw IllegalStateException("If the game is in progress, there should be at least one valid move.")
                }
            }
            when (chessGame.status) {
                Checkmate -> when( chessGame.position.turn ) {
                    White -> if(chessGame.white is AggressiveStupidComputerPlayer) 0f to 1f else 1f to 0f
                    Black -> if(chessGame.black is AggressiveStupidComputerPlayer) 0f to 1f else 1f to 0f
                }
                InProgress -> throw IllegalStateException("Game should be over!")
                else -> 0.5f to 0.5f
            }.let { (aggressiveStupid, stupid) ->
                stupidPoints += stupid
                aggressiveStupidPoints += aggressiveStupid
            }
            println("Aggressive stupid: $aggressiveStupidPoints, Minimax: $stupidPoints")
        }

    }

    @Test
    fun `minimax vs minimax with depth`() {
        var stupidPoints = 0f
        var aggressiveStupidPoints = 0f
        (1..1).map { numberOfGame ->
            var chessGame = if(numberOfGame % 2 == 0) {
                ChessGame(MinimaxWithDepthComputerPlayer, MinimaxComputerPlayer)
            } else {
                ChessGame(MinimaxComputerPlayer, MinimaxWithDepthComputerPlayer)
            }.chessGame
            while (chessGame.status == InProgress) {
                val player = chessGame.getActivePlayer() as ComputerPlayer
                when(val move = player.calculateMove(chessGame)) {
                    is MoveCalculated -> chessGame = chessGame.movePiece(move.move).chessGame
                    is NoMoveCalculated -> throw IllegalStateException("If the game is in progress, there should be at least one valid move.")
                }
            }
            when (chessGame.status) {
                Checkmate -> when( chessGame.position.turn ) {
                    White -> if(chessGame.white is MinimaxWithDepthComputerPlayer) 0f to 1f else 1f to 0f
                    Black -> if(chessGame.black is MinimaxWithDepthComputerPlayer) 0f to 1f else 1f to 0f
                }
                InProgress -> throw IllegalStateException("Game should be over!")
                else -> 0.5f to 0.5f
            }.let { (aggressiveStupid, stupid) ->
                stupidPoints += stupid
                aggressiveStupidPoints += aggressiveStupid
            }
            println("Minimax with depth: $aggressiveStupidPoints, Minimax: $stupidPoints")
        }

    }

    @Test
    fun `mate in two`() {
        val chessGame = ChessGame(
            position = Position(
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
        val result = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame.chessGame)
        when(result) {
            is MoveCalculated -> assertThat(result.move).isEqualTo(Move(f3, g5))
            is NoMoveCalculated -> fail("No move calculated.")
        }
        val updatedGame = result.chessGame.movePiece(Move(f3, g5)).movePiece(Move(f7, g8))

        val mate = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(updatedGame.chessGame)
        when(mate) {
            is MoveCalculated -> assertThat(mate.move).isEqualTo(Move(c2, h7))
            is NoMoveCalculated -> fail("No move calculated.")
        }
        assertThat(mate.movePiece(mate.move).chessGame.status).isEqualTo(Checkmate)

    }

    @Test
    fun `another mate in two`() {
        val chessGame = ChessGame(
            position = Position(
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
        val result = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame.chessGame)
        when(result) {
            is MoveCalculated -> assertThat(result.move).isIn(Move(d7, c8), Move(d7, e8))
            is NoMoveCalculated -> fail("No move calculated.")
        }
        val updatedGame = result.chessGame.movePiece(result.move).movePiece(Move(h8, result.move.arrivalSquare))

        val mate = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(updatedGame.chessGame)
        when(mate) {
            is MoveCalculated -> assertThat(mate.move).isEqualTo(Move(f6, d7))
            is NoMoveCalculated -> fail("No move calculated.")
        }
        assertThat(mate.movePiece(mate.move).chessGame.status).isEqualTo(Checkmate)

    }
}