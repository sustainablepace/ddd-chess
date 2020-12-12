package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.A1
import net.sustainablepace.chess.domain.aggregate.chessgame.A2
import net.sustainablepace.chess.domain.aggregate.chessgame.B1
import net.sustainablepace.chess.domain.aggregate.chessgame.B2
import net.sustainablepace.chess.domain.move.rules.Direction
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DirectionTest {
    @Test
    fun `test add`() {
        Assertions.assertThat(Direction.straightLine().from(A1)).isEqualTo(A2)
        Assertions.assertThat(Direction.straightLine().rotate(1).from(A1)).isEqualTo(B1)
        Assertions.assertThat(Direction.diagonal().from(A1)).isEqualTo(B2)
        Assertions.assertThat(Direction.straightLine().rotate(3).from(A1)).isNull()
        Assertions.assertThat(Direction.straightLine().rotate(2).from(A1)).isNull()
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