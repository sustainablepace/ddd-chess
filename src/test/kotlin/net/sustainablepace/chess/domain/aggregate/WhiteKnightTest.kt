package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteKnightTest {

    @Test
    fun `finds valid knight movements`() {
        val position = Position(mapOf(
                C2 to WhiteKnight,
                B4 to BlackPawn,
                E1 to WhiteKing
            )
        )
        val moves = position.moveOptionsIgnoringCheck(C2)
        Assertions.assertThat(moves).containsExactlyInAnyOrder(
            ValidMove(C2, D4),
            ValidMove(C2, B4),
            ValidMove(C2, E3),
            ValidMove(C2, A1),
            ValidMove(C2, A3)
        )
    }

}