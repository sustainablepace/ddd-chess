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

    @Test
    fun `bishop weight white`() {
        assertThat(Weight(a1, WhiteBishop)).isEqualTo(-2.0)
        assertThat(Weight(a4, WhiteBishop)).isEqualTo(-1.0)
        assertThat(Weight(e2, WhiteBishop)).isEqualTo(0.0)
        assertThat(Weight(h5, WhiteBishop)).isEqualTo(-1.0)
        assertThat(Weight(b3, WhiteBishop)).isEqualTo(1.0)
    }

    @Test
    fun `bishop weight black`() {
        assertThat(Weight(a1, BlackBishop)).isEqualTo(-2.0)
        assertThat(Weight(a4, BlackBishop)).isEqualTo(-1.0)
        assertThat(Weight(e2, BlackBishop)).isEqualTo(0.0)
        assertThat(Weight(h5, BlackBishop)).isEqualTo(-1.0)
        assertThat(Weight(b3, BlackBishop)).isEqualTo(0.0)
    }

    @Test
    fun `knight weight white`() {
        assertThat(Weight(a1, WhiteKnight)).isEqualTo(-5.0)
        assertThat(Weight(a4, WhiteKnight)).isEqualTo(-3.0)
        assertThat(Weight(e2, WhiteKnight)).isEqualTo(0.5)
        assertThat(Weight(h5, WhiteKnight)).isEqualTo(-3.0)
        assertThat(Weight(b3, WhiteKnight)).isEqualTo(0.5)
    }

    @Test
    fun `knight weight black`() {
        assertThat(Weight(a1, BlackKnight)).isEqualTo(-5.0)
        assertThat(Weight(a4, BlackKnight)).isEqualTo(-3.0)
        assertThat(Weight(e2, BlackKnight)).isEqualTo(0.0)
        assertThat(Weight(h5, BlackKnight)).isEqualTo(-3.0)
        assertThat(Weight(b3, BlackKnight)).isEqualTo(0.0)
    }

    @Test
    fun `rook weight white`() {
        assertThat(Weight(a1, WhiteRook)).isEqualTo(0.0)
        assertThat(Weight(a4, WhiteRook)).isEqualTo(-0.5)
        assertThat(Weight(e2, WhiteRook)).isEqualTo(0.0)
        assertThat(Weight(h5, WhiteRook)).isEqualTo(-0.5)
        assertThat(Weight(b3, WhiteRook)).isEqualTo(0.0)
    }

    @Test
    fun `rook weight black`() {
        assertThat(Weight(a1, BlackRook)).isEqualTo(0.0)
        assertThat(Weight(a4, BlackRook)).isEqualTo(-0.5)
        assertThat(Weight(e2, BlackRook)).isEqualTo(1.0)
        assertThat(Weight(h5, BlackRook)).isEqualTo(-0.5)
        assertThat(Weight(b3, BlackRook)).isEqualTo(0.0)
    }
}