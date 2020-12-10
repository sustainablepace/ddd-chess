package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.aggregate.chessgame.position.add
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DirectionTest {
    @Test
    fun `test add`() {
        Assertions.assertThat("a1".add(Direction.straightLine())).isEqualTo("a2")
        Assertions.assertThat("a1".add(Direction.straightLine().rotate(1))).isEqualTo("b1")
        Assertions.assertThat("a1".add(Direction.diagonal())).isEqualTo("b2")
        Assertions.assertThat("a1".add(Direction.straightLine().rotate(3))).isNull()
        Assertions.assertThat("a1".add(Direction.straightLine().rotate(2))).isNull()
    }

    @Test
    fun `rotate direction`() {
        Direction.straightLine().apply {
            Assertions.assertThat(x).isEqualTo(0)
            Assertions.assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(1)
            Assertions.assertThat(y).isEqualTo(0)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(0)
            Assertions.assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(-1)
            Assertions.assertThat(y).isEqualTo(0)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(0)
            Assertions.assertThat(y).isEqualTo(1)
        }

        Direction.diagonal().apply {
            Assertions.assertThat(x).isEqualTo(1)
            Assertions.assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(1)
            Assertions.assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(-1)
            Assertions.assertThat(y).isEqualTo(-1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(-1)
            Assertions.assertThat(y).isEqualTo(1)
        }.rotate(1).apply {
            Assertions.assertThat(x).isEqualTo(1)
            Assertions.assertThat(y).isEqualTo(1)
        }

    }

}