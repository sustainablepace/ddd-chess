package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhiteQueen
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhitePawnTest {
    @Test
    fun `finds valid pawn movements on empty board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhitePawn()
        )))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-e5") as ValidMove
        )
    }

    @Test
    fun `finds valid pawn movements on crowded board`() {
        val chessGame = ChessGame(Position(mapOf(
            "e4" to WhitePawn(),
            "e5" to WhiteQueen(),
            "d5" to BlackPawn(),
            "f5" to BlackPawn()
        )))
        val moves = chessGame.findMoves("e4")
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove("e4-f5") as ValidMove,
            ValidMove("e4-d5") as ValidMove
        )
    }

    // TODO: Allow two steps forward if pawn has never been moved
    // TODO: Allow promotion

}