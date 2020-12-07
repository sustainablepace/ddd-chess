package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.domain.Move
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MovePieceTest {
    @Test
    fun `ignore invalid move`() {
        val result = MovePiece("id", "invalid")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `ignore move to same square`() {
        val result = MovePiece("id", "e2-e2")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `ignore moves with invalid square`() {
        val result = MovePiece("id", "e2-e9")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `e2-e4 is a valid move`() {
        val command = MovePiece("id", "e2-e4").getOrNull()

        assertThat(command).isInstanceOf(MovePiece::class.java)
        assertThat(command?.move).isEqualTo(Move("e2-e4").getOrNull())
    }
}