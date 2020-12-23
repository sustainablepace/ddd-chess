package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WeightTest {

    @Test
    fun `pawn weight white`() {
        assertThat(Weight(a1, WhitePawn)).isEqualTo(0.0)
        assertThat(Weight(e2, WhitePawn)).isEqualTo(-2.0)
        assertThat(Weight(h5, WhitePawn)).isEqualTo(0.5)
    }

    @Test
    fun `pawn weight black`() {
        assertThat(Weight(a1, BlackPawn)).isEqualTo(0.0)
        assertThat(Weight(e2, BlackPawn)).isEqualTo(5.0)
        assertThat(Weight(h5, BlackPawn)).isEqualTo(0.0)
    }

    @Test
    fun `king weight white`() {
        assertThat(Weight(a1, WhiteKing)).isEqualTo(2.0)
        assertThat(Weight(e2, WhiteKing)).isEqualTo(0.0)
        assertThat(Weight(h5, WhiteKing)).isEqualTo(-3.0)
    }

    @Test
    fun `king weight black`() {
        assertThat(Weight(a1, BlackKing)).isEqualTo(-3.0)
        assertThat(Weight(e2, BlackKing)).isEqualTo(-5.0)
        assertThat(Weight(h5, BlackKing)).isEqualTo(-2.0)
    }

    @Test
    fun `queen weight white`() {
        assertThat(Weight(a1, WhiteQueen)).isEqualTo(-2.0)
        assertThat(Weight(a4, WhiteQueen)).isEqualTo(0.0)
        assertThat(Weight(e2, WhiteQueen)).isEqualTo(0.0)
        assertThat(Weight(h5, WhiteQueen)).isEqualTo(-0.5)
        assertThat(Weight(b3, WhiteQueen)).isEqualTo(0.5)
    }

    @Test
    fun `queen weight black`() {
        assertThat(Weight(a1, BlackQueen)).isEqualTo(-2.0)
        assertThat(Weight(a4, BlackQueen)).isEqualTo(-0.5)
        assertThat(Weight(e2, BlackQueen)).isEqualTo(0.0)
        assertThat(Weight(h5, BlackQueen)).isEqualTo(-0.5)
        assertThat(Weight(b3, BlackQueen)).isEqualTo(0.0)
    }
}