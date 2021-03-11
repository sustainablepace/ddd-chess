package net.sustainablepace.chess.domain.move

import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.move.rules.Direction
import net.sustainablepace.chess.domain.move.rules.diff
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DirectionTest {
    @Test
    fun `test add`() {
        assertThat(Direction.straightLine().from(a1)).isEqualTo(a2)
        assertThat(Direction.straightLine().rotate(1).from(a1)).isEqualTo(b1)
        assertThat(Direction.diagonal().from(a1)).isEqualTo(b2)
        assertThat(Direction.straightLine().rotate(3).from(a1)).isNull()
        assertThat(Direction.straightLine().rotate(2).from(a1)).isNull()
    }

    @Test
    fun `rotate direction`() {
        Direction.straightLine().apply {
            assertThat(x).isEqualTo(0)
            assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(1)
            assertThat(y).isEqualTo(0)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(0)
            assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(-1)
            assertThat(y).isEqualTo(0)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(0)
            assertThat(y).isEqualTo(1)
        }

        Direction.diagonal().apply {
            assertThat(x).isEqualTo(1)
            assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(1)
            assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(-1)
            assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(-1)
            assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            assertThat(x).isEqualTo(1)
            assertThat(y).isEqualTo(1)
        }

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