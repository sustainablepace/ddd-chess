package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun `insufficient material`() {
        assertThat(mapOf<Square, Piece>().containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf(E1 to WhitePawn, E2 to WhitePawn).containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf(E1 to BlackPawn, E2 to BlackPawn).containsBothWhiteAndBlackPieces()).isFalse()
        assertThat(mapOf(E1 to WhitePawn, E2 to BlackPawn).containsBothWhiteAndBlackPieces()).isTrue()
    }

}