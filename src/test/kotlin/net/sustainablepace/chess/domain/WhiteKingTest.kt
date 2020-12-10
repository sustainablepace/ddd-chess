package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteKing
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteQueen
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteKingTest {
    @Test
    fun `finds valid king movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteKing()
        )))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-e5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }

    @Test
    fun `finds valid king movements on crowded board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhiteKing(),
            "d5" to BlackPawn(),
            "e5" to WhiteQueen()
        )))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-d3") as ValidMove,
            ValidMove("e4-e3") as ValidMove,
            ValidMove("e4-f3") as ValidMove,
            ValidMove("e4-f4") as ValidMove,
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove,
            ValidMove("e4-d4") as ValidMove
        )
    }

    // TODO: Allow castling

}