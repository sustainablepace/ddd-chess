package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.rules.Direction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SquareTest {
    @Test
    fun `All squares exist`() {
        assertThat(Square('a', 1)).isEqualTo(a1)
        assertThat(Square('a', 2)).isEqualTo(a2)
    }
    @Test
    fun `compute difference between two squares`() {
        assertThat(a1 diff a1).isEqualTo(Direction(0,0))
        assertThat(a1 diff a2).isEqualTo(Direction(0,1))
        assertThat(a1 diff a8).isEqualTo(Direction(0,7))
        assertThat(a1 diff h1).isEqualTo(Direction(7,0))
        assertThat(a1 diff h8).isEqualTo(Direction(7,7))
        assertThat(a8 diff h1).isEqualTo(Direction(7,7))
    }
}