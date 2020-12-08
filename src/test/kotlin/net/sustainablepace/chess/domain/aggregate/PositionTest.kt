package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.position.BlackPawn
import net.sustainablepace.chess.domain.aggregate.chessgame.Position
import net.sustainablepace.chess.domain.aggregate.chessgame.position.WhitePawn
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `insufficient material`() {
        assertThat(Position(emptyMap()).containsWhiteAndBlackPieces()).isFalse()
        assertThat(Position(mapOf("e1" to WhitePawn())).containsWhiteAndBlackPieces()).isFalse()
        assertThat(Position(mapOf("e1" to BlackPawn())).containsWhiteAndBlackPieces()).isFalse()
        assertThat(Position(mapOf("e1" to WhitePawn(), "e2" to BlackPawn())).containsWhiteAndBlackPieces()).isTrue()
    }

}