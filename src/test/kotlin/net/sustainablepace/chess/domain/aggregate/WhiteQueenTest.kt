package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteQueenTest {

    @Test
    fun `finds valid queen movements on empty board`() {
        val position = Position(mapOf(
                E4 to WhiteQueen
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
            Move(E4, H1),
            Move(E4, E5),
            Move(E4, E6),
            Move(E4, E7),
            Move(E4, E8),
            Move(E4, E3),
            Move(E4, E2),
            Move(E4, E1),
            Move(E4, d4),
            Move(E4, c4),
            Move(E4, b4),
            Move(E4, a4),
            Move(E4, F4),
            Move(E4, G4),
            Move(E4, H4)

        )
    }

    @Test
    fun `finds valid queen movements on crowded board`() {
        val position = Position(mapOf(
                E4 to WhiteQueen,
                d5 to BlackPawn,
                G2 to WhiteBishop,
                a4 to WhiteKing,
                G4 to BlackPawn,
                E1 to WhiteRook,
                E6 to BlackQueen
            )
        )
        val moves = position.moveOptionsIgnoringCheck(E4)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            Move(E4, d3),
            Move(E4, c2),
            Move(E4, b1),
            Move(E4, d5),
            Move(E4, F5),
            Move(E4, G6),
            Move(E4, H7),
            Move(E4, F3),
            Move(E4, d4),
            Move(E4, c4),
            Move(E4, b4),
            Move(E4, F4),
            Move(E4, G4),
            Move(E4, E3),
            Move(E4, E2),
            Move(E4, E5),
            Move(E4, E6)
        )
    }
}