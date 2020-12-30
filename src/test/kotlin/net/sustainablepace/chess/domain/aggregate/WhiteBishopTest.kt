package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteBishopTest {
    @Test
    fun `finds valid bishop movements on empty board`() {
        val position = position(mapOf(
                e4 to WhiteBishop
            )
        )
        val moves = position.moveOptionsForSquare(e4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(e4, d3),
            Move(e4, c2),
            Move(e4, b1),
            Move(e4, d5),
            Move(e4, c6),
            Move(e4, b7),
            Move(e4, a8),
            Move(e4, f5),
            Move(e4, g6),
            Move(e4, h7),
            Move(e4, f3),
            Move(e4, g2),
            Move(e4, h1)
        )
    }

    @Test
    fun `finds valid bishop movements on crowded board`() {
        val position = position(mapOf(
                e4 to WhiteBishop,
                d5 to BlackPawn,
                g2 to WhiteQueen
            )
        )
        val moves = position.moveOptionsForSquare(e4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(e4,d3),
            Move(e4,c2),
            Move(e4,b1),
            Move(e4,d5),
            Move(e4,f5),
            Move(e4,g6),
            Move(e4,h7),
            Move(e4,f3)
        )
    }
}