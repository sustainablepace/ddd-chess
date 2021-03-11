package net.sustainablepace.chess.application.port.`in`

import net.sustainablepace.chess.application.port.`in`.command.MovePiece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
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
        assertThat(command?.move).isEqualTo(Move("e2-e4"))
    }

    @Test
    fun `promotion to white queen`() {
        val command = MovePiece("id", "e7-e8Q").getOrNull()

        assertThat(command).isInstanceOf(MovePiece::class.java)
        assertThat(command?.move).isEqualTo(PromotionMove(e7, e8, WhiteQueen))
    }

    @Test
    fun `promotion to black queen`() {
        val command = MovePiece("id", "e2-e1Q").getOrNull()

        assertThat(command).isInstanceOf(MovePiece::class.java)
        assertThat(command?.move).isEqualTo(PromotionMove(e2, e1, BlackQueen))
    }
}