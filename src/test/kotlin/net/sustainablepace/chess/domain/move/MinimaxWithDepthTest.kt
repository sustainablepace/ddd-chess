package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.MoveCalculated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class MinimaxWithDepthTest {
    @Test
    fun `find best move`() {
        // see https://www.freecodecamp.org/news/simple-chess-ai-step-by-step-1d55a9266977/
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    b1 to WhiteBishop,
                    a2 to BlackKnight,
                    c2 to BlackBishop,
                    a3 to BlackRook,
                    e8 to BlackKing,
                    h1 to WhiteKing
                )
            )
        )

        when(val moveCalculated = MinimaxWithDepthAndSophisticatedEvaluationComputerPlayer.calculateMove(chessGame.chessGame)){
            is MoveCalculated -> assertThat(moveCalculated.move).isEqualTo(Move(b1, c2))
            else -> fail("no move calculated")
        }
    }
}