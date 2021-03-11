package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PieceTest {
    @Test
    fun `test hashCode`() {
        assertThat(WhitePawn.hashCode()).isEqualTo(WhitePawn.hashCode())
        assertThat(WhitePawn.hashCode()).isNotEqualTo(BlackPawn.hashCode())
    }

    @Test
    fun `is promotion option`() {
        assertThat(WhitePawn.isPromotionOption).isFalse()
        assertThat(BlackPawn.isPromotionOption).isFalse()
        assertThat(WhiteKing.isPromotionOption).isFalse()
        assertThat(BlackKing.isPromotionOption).isFalse()

        assertThat(WhiteBishop.isPromotionOption).isTrue()
        assertThat(BlackBishop.isPromotionOption).isTrue()
        assertThat(WhiteKnight.isPromotionOption).isTrue()
        assertThat(BlackKnight.isPromotionOption).isTrue()
        assertThat(WhiteRook.isPromotionOption).isTrue()
        assertThat(BlackRook.isPromotionOption).isTrue()
        assertThat(WhiteQueen.isPromotionOption).isTrue()
        assertThat(BlackQueen.isPromotionOption).isTrue()
    }
}