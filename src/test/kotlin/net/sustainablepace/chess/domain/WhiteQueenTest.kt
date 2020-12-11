package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteQueenTest {

    @Test
    fun `finds valid queen movements on empty board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteQueen()
        ))
        val moves = chessGame.findMoves("e4")
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
            ValidMove("e4-h1") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-e6") as ValidMove,
            ValidMove("e4-e7") as ValidMove,
            ValidMove("e4-e8") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-e2") as ValidMove,
            ValidMove("e4-e1") as ValidMove,
            ValidMove("e4-d4") as ValidMove,
            ValidMove("e4-c4") as ValidMove,
            ValidMove("e4-b4") as ValidMove,
            ValidMove("e4-a4") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-g4") as ValidMove,
            ValidMove("e4-h4") as ValidMove

        )
    }

    @Test
    fun `finds valid queen movements on crowded board`() {
        val chessGame = ChessGame(mapOf(
            "e4" to WhiteQueen(),
            "d5" to BlackPawn(),
            "g2" to WhiteBishop(),
            "a4" to WhiteKing(),
            "g4" to BlackPawn(),
            "e1" to WhiteRook(),
            "e6" to BlackQueen()
        ))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-c2") as ValidMove,
            ValidMove("e4-b1") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-g6") as ValidMove,
            ValidMove("e4-h7") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-d4") as ValidMove,
            ValidMove("e4-c4") as ValidMove,
            ValidMove("e4-b4") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-g4") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-e2") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-e6") as ValidMove
        )
    }
}