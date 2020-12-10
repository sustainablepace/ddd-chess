package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteQueen
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class BlackPawnTest {
    @Test
    fun `finds valid pawn movements on empty board in initial position`() {
        val chessGame = ChessGame(Position(mapOf(
            "e7" to BlackPawn()
        )))
        val moves = chessGame.findMoves("e7")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e7-e6") as ValidMove,
            ValidMove("e7-e5") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e5" to BlackPawn()
        )))
        val moves = chessGame.findMoves("e5")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e5-e4") as ValidMove
        )
    }

}