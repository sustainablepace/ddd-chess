package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.WhitePawn
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PieceTest {
    @Test
    fun `test hashCode`() {
        assertThat(WhitePawn.hashCode()).isEqualTo(WhitePawn.hashCode())
        assertThat(WhitePawn.hashCode()).isNotEqualTo(BlackPawn.hashCode())
    }
}