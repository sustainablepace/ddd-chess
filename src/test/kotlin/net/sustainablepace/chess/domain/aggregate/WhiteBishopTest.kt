package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
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
            Move(E4, d3),
            Move(E4, c2),
            Move(E4, b1),
            Move(E4, d5),
            Move(E4, c6),
            Move(E4, b7),
            Move(E4, a8),
            Move(E4, F5),
            Move(E4, G6),
            Move(E4, H7),
            Move(E4, F3),
            Move(E4, G2),
            Move(E4, H1)
        )
    }

    @Test
    fun `finds valid bishop movements on crowded board`() {
        val position = Position(mapOf(
                E4 to WhiteBishop,
                d5 to BlackPawn,
                G2 to WhiteQueen
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(E4,d3),
            Move(E4,c2),
            Move(E4,b1),
            Move(E4,d5),
            Move(E4,F5),
            Move(E4,G6),
            Move(E4,H7),
            Move(E4,F3)
        )
    }
}