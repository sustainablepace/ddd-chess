package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteBishop
import net.sustainablepace.chess.domain.aggregate.chessgame.WhiteQueen
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteBishopTest {
    @Test
    fun `finds valid bishop movements on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteBishop
        ))
        val moves = chessGame.moveOptions("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-c6") as ValidMove,
            ValidMove("e4-b7") as ValidMove,
            ValidMove("e4-a8") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-g2") as ValidMove,
            ValidMove("e4-h1") as ValidMove
        )
    }

    @Test
    fun `finds valid bishop movements on crowded board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteBishop,
            "d5" to BlackPawn,
            "g2" to WhiteQueen
        ))
        val moves = chessGame.moveOptions("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove
        )
    }
}