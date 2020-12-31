package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessGame
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.MoveCalculated
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class MinimaxTest {
    @Test
    fun `find best move`() {
        // see https://www.freecodecamp.org/news/simple-chess-ai-step-by-step-1d55a9266977/
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    b1 to WhiteBishop,
                    a2 to BlackKnight,
                    c2 to BlackBishop,
                    a3 to BlackRook
                )
            )
        )

        when(val moveCalculated = MinimaxComputerPlayer.calculateMove(chessGame.chessGame)){
            is MoveCalculated -> assertThat(moveCalculated.move).isEqualTo(Move(b1, c2))
            else -> fail("no move calculated")
        }
    }

    @Test
    fun `find best move minimax with depth`() {
        // see https://www.freecodecamp.org/news/simple-chess-ai-step-by-step-1d55a9266977/
        val chessGame = chessGame(
            position = position(
                board = mapOf(
                    b1 to WhiteBishop,
                    a2 to BlackKnight,
                    c2 to BlackBishop,
                    a3 to BlackRook
                )
            )
        )

        when(val moveCalculated = MinimaxComputerPlayer.calculateMove(chessGame.chessGame)){
            is MoveCalculated -> assertThat(moveCalculated.move).isEqualTo(Move(b1, c2))
            else -> fail("no move calculated")
        }
    }
}