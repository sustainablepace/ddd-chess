package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.Move
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WhiteKnightTest {

    @Test
    fun `finds valid knight movements`() {
        val position = position(mapOf(
                c2 to WhiteKnight,
                b4 to BlackPawn,
                e1 to WhiteKing
            )
        )
        Assertions.assertThat(position.moveOptions.filter { it.departureSquare == c2 }).containsExactlyInAnyOrder(
            Move(c2, d4),
            Move(c2, b4),
            Move(c2, e3),
            Move(c2, a1),
            Move(c2, a3)
        )
    }

}