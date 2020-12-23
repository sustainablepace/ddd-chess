package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.Square
import net.sustainablepace.chess.domain.aggregate.chessgame.a1
import net.sustainablepace.chess.domain.aggregate.chessgame.a2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SquareTest {
    @Test
    fun `All squares exist`() {
        assertThat(Square('a', 1)).isEqualTo(a1)
        assertThat(Square('a', 2)).isEqualTo(a2)
    }
}