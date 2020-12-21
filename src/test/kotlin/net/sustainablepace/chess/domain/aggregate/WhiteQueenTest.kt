package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteQueenTest {

    @Test
    fun `finds valid queen movements on empty board`() {
        val position = Position(mapOf(
                e4 to WhiteQueen
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
            Move(e4, h1),
            Move(e4, e5),
            Move(e4, e6),
            Move(e4, e7),
            Move(e4, e8),
            Move(e4, e3),
            Move(e4, e2),
            Move(e4, e1),
            Move(e4, d4),
            Move(e4, c4),
            Move(e4, b4),
            Move(e4, a4),
            Move(e4, f4),
            Move(e4, g4),
            Move(e4, h4)

        )
    }

    @Test
    fun `finds valid queen movements on crowded board`() {
        val position = Position(mapOf(
                e4 to WhiteQueen,
                d5 to BlackPawn,
                g2 to WhiteBishop,
                a4 to WhiteKing,
                g4 to BlackPawn,
                e1 to WhiteRook,
                e6 to BlackQueen
            )
        )
        val moves = position.moveOptionsForSquare(e4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(e4, d3),
            Move(e4, c2),
            Move(e4, b1),
            Move(e4, d5),
            Move(e4, f5),
            Move(e4, g6),
            Move(e4, h7),
            Move(e4, f3),
            Move(e4, d4),
            Move(e4, c4),
            Move(e4, b4),
            Move(e4, f4),
            Move(e4, g4),
            Move(e4, e3),
            Move(e4, e2),
            Move(e4, e5),
            Move(e4, e6)
        )
    }
}