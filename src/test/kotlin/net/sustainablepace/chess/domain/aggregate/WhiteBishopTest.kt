package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteBishopTest {
    @Test
    fun `finds valid bishop movements on empty board`() {
        val position = Position(mapOf(
                E4 to WhiteBishop
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4, D3),
            ValidMove(E4, C2),
            ValidMove(E4, B1),
            ValidMove(E4, D5),
            ValidMove(E4, C6),
            ValidMove(E4, B7),
            ValidMove(E4, A8),
            ValidMove(E4, F5),
            ValidMove(E4, G6),
            ValidMove(E4, H7),
            ValidMove(E4, F3),
            ValidMove(E4, G2),
            ValidMove(E4, H1)
        )
    }

    @Test
    fun `finds valid bishop movements on crowded board`() {
        val position = Position(mapOf(
                E4 to WhiteBishop,
                D5 to BlackPawn,
                G2 to WhiteQueen
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(E4,D3),
            ValidMove(E4,C2),
            ValidMove(E4,B1),
            ValidMove(E4,D5),
            ValidMove(E4,F5),
            ValidMove(E4,G6),
            ValidMove(E4,H7),
            ValidMove(E4,F3)
        )
    }
}