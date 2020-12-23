package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.ChessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.MoveCalculated
import net.sustainablepace.chess.domain.event.NoMoveCalculated
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

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
    @Disabled
    fun `minimax vs minimax with depth`() {
        var stupidPoints = 0f
        var aggressiveStupidPoints = 0f
        (1..2).map { numberOfGame ->
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
}