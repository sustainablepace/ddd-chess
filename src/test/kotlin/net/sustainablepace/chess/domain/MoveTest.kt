package net.sustainablepace.chess.domain

import net.sustainablepace.chess.domain.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MoveTest {
    @Test
    fun `ignore invalid move`() {
        val result = Move("invalid")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `ignore move to same square`() {
        val result = Move("e2-e2")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `ignore moves with invalid square`() {
        val result = Move("e2-e9")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `e2-e4 is a valid move`() {
        val move = Move("e2-e4").getOrNull()

        assertThat(move?.departureSquare).isEqualTo("e2")
        assertThat(move?.arrivalSquare).isEqualTo("e4")
    }
}