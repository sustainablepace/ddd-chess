package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `insufficient material`() {
        assertThat(mapOf<Square, Piece>().containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf("e1" to WhitePawn).containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf("e1" to BlackPawn).containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf("e1" to WhitePawn, "e2" to BlackPawn).containsBothWhiteAndBlackPieces()).isTrue()
    }

}