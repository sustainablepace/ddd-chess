package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.a1
import net.sustainablepace.chess.domain.aggregate.chessgame.a2
import net.sustainablepace.chess.domain.aggregate.chessgame.b1
import net.sustainablepace.chess.domain.aggregate.chessgame.b2
import net.sustainablepace.chess.domain.move.rules.Direction
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DirectionTest {
    @Test
    fun `test add`() {
        Assertions.assertThat(Direction.straightLine().from(a1)).isEqualTo(a2)
        Assertions.assertThat(Direction.straightLine().rotate(1).from(a1)).isEqualTo(b1)
        Assertions.assertThat(Direction.diagonal().from(a1)).isEqualTo(b2)
        Assertions.assertThat(Direction.straightLine().rotate(3).from(a1)).isNull()
        Assertions.assertThat(Direction.straightLine().rotate(2).from(a1)).isNull()
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